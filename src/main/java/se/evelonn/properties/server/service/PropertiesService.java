package se.evelonn.properties.server.service;

import java.io.FileInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.evelonn.properties.server.PropertiesServerConfiguration;

public class PropertiesService {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesService.class);

    private PropertiesServerConfiguration propertiesServerConfiguration;

    private Properties properties = new Properties();

    private Git git;

    private Path pathToProperties = Paths.get("").resolve("properties");

    public PropertiesService(PropertiesServerConfiguration configuration) {
	this.propertiesServerConfiguration = configuration;

	cloneRepository();
	update();
    }

    public String getProperty(String propertyKey) {
	return properties.getProperty(propertyKey);
    }

    private void cloneRepository() {
	if (git == null && pathToProperties.toFile().isDirectory()) {
	    try {
		this.git = Git.open(pathToProperties.toFile());
		logger.info("Opened GIT-repo at " + pathToProperties.toFile().getAbsolutePath());
	    } catch (Exception e) {
		throw new PropertiesServerException("Failed to open properties repoository: " + e.getMessage(), e);
	    }
	} else {
	    try {
		this.git = Git.cloneRepository().setDirectory(pathToProperties.toFile())
			.setURI(propertiesServerConfiguration.getGitUrl()).call();
		logger.info("Cloned " + propertiesServerConfiguration.getGitUrl() + " to "
			+ pathToProperties.toFile().getAbsolutePath());
	    } catch (Exception e) {
		throw new PropertiesServerException("Failed to clone git repository: " + e.getMessage(), e);
	    }
	}
    }

    private void addPropertiesFromDirectory(Path pathToProperties) {

	try (DirectoryStream<Path> streamedPaths = Files.newDirectoryStream(pathToProperties)) {
	    streamedPaths.forEach(path -> {
		if (path.toFile().isDirectory() && !path.toFile().getName().equals(".git")) {
		    addPropertiesFromDirectory(path);
		} else if (path.toFile().getName().endsWith(".properties")) {
		    try {
			Properties tmpProperties = new Properties();
			tmpProperties.load(new FileInputStream(path.toFile()));
			properties.putAll(tmpProperties);
			logger.info("Added properties from: " + path.toFile().getAbsolutePath());
		    } catch (Exception e) {
			throw new PropertiesServerException("Failed to read property file "
				+ path.toFile().getAbsolutePath() + ": " + e.getMessage(), e);
		    }
		}
	    });
	} catch (Exception e) {
	    throw new PropertiesServerException(
		    "Failed to read directory " + pathToProperties.toFile().getAbsolutePath() + ": " + e.getMessage(),
		    e);
	}
    }

    public void clear() {
	this.properties.clear();
	logger.info("Cleared properties");
    }

    private void update() {
	clear();
	addPropertiesFromDirectory(pathToProperties);
	logger.info("Updated properties");
    }

    public void pullAndReload() {
	PullResult pullResult;
	try {
	    pullResult = git.pull().call();
	} catch (Exception e) {
	    throw new PropertiesServerException("Failed to perform pull from remote: " + e.getMessage(), e);
	}
	if (!pullResult.isSuccessful()) {
	    throw new PropertiesServerException(
		    "Failed to execute Pull from remote: " + pullResult.getFetchResult().getMessages());
	}
	logger.info("Pulled successfully");
	clear();
	update();
    }

    public boolean isHealthy() {
	return git != null && !properties.isEmpty();
    }
}