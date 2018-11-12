package se.evelonn.properties.server.service;

import java.io.FileInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesService {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesService.class);

    private final GitService gitService;

    private Properties properties = new Properties();

    public PropertiesService(GitService gitService) {
	this.gitService = gitService;

	update();
    }

    public String getProperty(String propertyKey) {
	return properties.getProperty(propertyKey);
    }

    public Properties properties() {
	return properties;
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

    public void update() {
	clear();
	addPropertiesFromDirectory(gitService.localDirectory().toPath());
	logger.info("Updated properties");
    }

    public void pullAndReload() {
	gitService.pull();
	logger.info("Pulled successfully");
	clear();
	update();
    }

    public boolean isHealthy() {
	return !properties.isEmpty();
    }
}