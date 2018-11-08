package se.evelonn.properties.server;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import se.evelonn.properties.server.health.PropertiesServerHealthCheck;
import se.evelonn.properties.server.resources.PropertyResource;
import se.evelonn.properties.server.service.PropertiesService;
import se.evelonn.properties.server.tasks.ClearTask;
import se.evelonn.properties.server.tasks.PullAndReloadTask;

public class PropertiesServerApplication extends Application<PropertiesServerConfiguration> {

    public static void main(String[] args) throws Exception {
	new PropertiesServerApplication().run(args);
    }

    @Override
    public void run(final PropertiesServerConfiguration configuration, Environment environment) throws Exception {
	PropertiesService propertiesService = new PropertiesService(configuration);

	environment.jersey().register(new PropertyResource(propertiesService));

	environment.healthChecks().register("properties-server", new PropertiesServerHealthCheck(propertiesService));

	environment.admin().addTask(new PullAndReloadTask(propertiesService));
	environment.admin().addTask(new ClearTask(propertiesService));
    }
}