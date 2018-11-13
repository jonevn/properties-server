package com.github.jonevn.properties.server.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class PropertiesServerConfiguration extends Configuration {

    @NotNull
    @JsonProperty
    private GitConfiguration git;

    @NotNull
    @JsonProperty
    private PropertiesConfiguration properties;

    public GitConfiguration getGit() {
	return git;
    }

    public void setGit(GitConfiguration git) {
	this.git = git;
    }

    public PropertiesConfiguration getProperties() {
	return properties;
    }

    public void setProperties(PropertiesConfiguration properties) {
	this.properties = properties;
    }

    @Override
    public String toString() {
	return String.format("PropertiesServerConfiguration [git=%s, properties=%s]", git, properties);
    }
}