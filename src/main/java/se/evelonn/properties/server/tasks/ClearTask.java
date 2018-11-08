package se.evelonn.properties.server.tasks;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMultimap;

import io.dropwizard.servlets.tasks.Task;
import se.evelonn.properties.server.service.PropertiesService;

public class ClearTask extends Task {

    private final PropertiesService propertiesService;

    public ClearTask(PropertiesService propertiesService) {
	super("clear");
	this.propertiesService = propertiesService;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
	try {
	    propertiesService.clear();
	    output.write("Properties cleared");
	} catch (Exception e) {
	    output.write("Failed to clear properties: " + e.getMessage());
	}
    }
}