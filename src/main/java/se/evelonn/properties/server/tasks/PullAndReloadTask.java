package se.evelonn.properties.server.tasks;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMultimap;

import io.dropwizard.servlets.tasks.Task;
import se.evelonn.properties.server.service.PropertiesService;

public class PullAndReloadTask extends Task {

    private PropertiesService propertiesService;

    public PullAndReloadTask(PropertiesService propertiesService) {
	super("PullAndUpdate");
	this.propertiesService = propertiesService;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
	try {
	    propertiesService.pullAndReload();
	    output.write("Properties reloaded");
	} catch (Exception e) {
	    output.write("Failed to pull and update properties: " + e.getMessage());
	}
    }
}