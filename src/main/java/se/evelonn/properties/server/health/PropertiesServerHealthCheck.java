package se.evelonn.properties.server.health;

import com.codahale.metrics.health.HealthCheck;

import se.evelonn.properties.server.service.PropertiesService;

public class PropertiesServerHealthCheck extends HealthCheck {

    private final PropertiesService propertiesService;

    public PropertiesServerHealthCheck(PropertiesService propertiesService) {
	this.propertiesService = propertiesService;
    }

    @Override
    protected Result check() throws Exception {
	return propertiesService.isHealthy() ? Result.healthy() : Result.unhealthy("Properties server not ok");
    }
}