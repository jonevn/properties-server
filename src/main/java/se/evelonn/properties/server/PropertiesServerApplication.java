package se.evelonn.properties.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import se.evelonn.properties.server.config.PropertiesServerConfiguration;
import se.evelonn.properties.server.health.PropertiesServerHealthCheck;
import se.evelonn.properties.server.resources.AdminViewResource;
import se.evelonn.properties.server.resources.PropertyResource;
import se.evelonn.properties.server.service.GitService;
import se.evelonn.properties.server.service.PropertiesService;
import se.evelonn.properties.server.tasks.ClearTask;
import se.evelonn.properties.server.tasks.PullAndReloadTask;

public class PropertiesServerApplication extends Application<PropertiesServerConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesServerApplication.class);

    public static void main(String[] args) throws Exception {
	new PropertiesServerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<PropertiesServerConfiguration> bootstrap) {
	bootstrap.addBundle(new ViewBundle<PropertiesServerConfiguration>());
    }

    @Override
    public void run(final PropertiesServerConfiguration configuration, Environment environment) throws Exception {
	logger.info("Starting application with configuration:\n" + configuration);

	GitService gitService = new GitService(configuration.getGit());
	PropertiesService propertiesService = new PropertiesService(configuration.getProperties(), gitService);

	// Resources
	environment.jersey().register(new PropertyResource(propertiesService));

	// View resources
	environment.jersey().register(new AdminViewResource(propertiesService, gitService));

	// Health checks
	environment.healthChecks().register("properties-server", new PropertiesServerHealthCheck(propertiesService));

	// Tasks
	environment.admin().addTask(new PullAndReloadTask(propertiesService));
	environment.admin().addTask(new ClearTask(propertiesService));
    }
}