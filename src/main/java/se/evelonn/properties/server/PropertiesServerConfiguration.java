package se.evelonn.properties.server;

import javax.annotation.Nonnull;

import io.dropwizard.Configuration;

public class PropertiesServerConfiguration extends Configuration {

    @Nonnull
    private String gitUrl;

    public void setGitUrl(String gitUrl) {
	this.gitUrl = gitUrl;
    }

    public String getGitUrl() {
	return gitUrl;
    }
}