package se.evelonn.properties.server.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertiesConfiguration {

    @NotEmpty
    @JsonProperty
    private String filePattern;

    @NotEmpty
    @JsonProperty
    private String includedFolders;

    public String getFilePattern() {
	return filePattern;
    }

    public void setFilePattern(String filePattern) {
	this.filePattern = filePattern;
    }

    public String getIncludedFolders() {
	return includedFolders;
    }

    public void setIncludedFolders(String includedFolders) {
	this.includedFolders = includedFolders;
    }

    @Override
    public String toString() {
	return String.format("PropertiesConfiguration [filePattern=%s, includedFolders=%s]", filePattern,
		includedFolders);
    }
}