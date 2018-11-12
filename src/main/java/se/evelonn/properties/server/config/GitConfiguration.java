package se.evelonn.properties.server.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitConfiguration {

    @NotEmpty
    @JsonProperty
    private String repositoryUrl;

    @NotEmpty
    @JsonProperty
    private String branch;

    @NotEmpty
    @JsonProperty
    private String pathToCloneTo;

    public String getRepositoryUrl() {
	return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
	this.repositoryUrl = repositoryUrl;
    }

    public String getBranch() {
	return branch;
    }

    public void setBranch(String branch) {
	this.branch = branch;
    }

    public String getPathToCloneTo() {
	return pathToCloneTo;
    }

    public void setPathToCloneTo(String pathToCloneTo) {
	this.pathToCloneTo = pathToCloneTo;
    }

    @Override
    public String toString() {
	return String.format("GitConfiguration [repositoryUrl=%s, branch=%s, pathToCloneTo=%s]", repositoryUrl, branch,
		pathToCloneTo);
    }
}
