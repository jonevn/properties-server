package se.evelonn.properties.server.config;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertiesConfiguration {

    @JsonProperty
    private String[] excludedFilePatterns;

    public List<String> getExcludedFilePatterns() {
	return Arrays.asList(excludedFilePatterns);
    }

    public void setExcludedFilePatterns(String[] excludedFilePatterns) {
	this.excludedFilePatterns = excludedFilePatterns;
    }

    @Override
    public String toString() {
	return String.format("PropertiesConfiguration [excludedFilePatterns=%s]",
		Arrays.toString(excludedFilePatterns));
    }
}