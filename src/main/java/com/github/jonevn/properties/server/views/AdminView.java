package com.github.jonevn.properties.server.views;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.ListBranchCommand.ListMode;

import com.github.jonevn.properties.server.service.Branch;
import com.github.jonevn.properties.server.service.GitService;
import com.github.jonevn.properties.server.service.PropertiesService;

import io.dropwizard.views.View;

public class AdminView extends View {

    private final PropertiesService propertiesService;
    private final GitService gitService;

    public AdminView(PropertiesService propertiesService, GitService gitService) {
	super("admin.mustache");
	this.propertiesService = propertiesService;
	this.gitService = gitService;
    }

    public String getCurrentBranch() {
	return gitService.currentBranch();
    }

    public Set<Branch> getAvailableBranches() {
	return gitService.availableBranches(ListMode.ALL);
    }

    public List<Property> getProperties() {
	return propertiesService.properties().entrySet().stream()
		.map(entry -> new Property((String) entry.getKey(), (String) entry.getValue()))
		.collect(Collectors.toList());
    }

    public void clear() {
	propertiesService.clear();
    }

    public void update() {
	propertiesService.pullAndReload();
    }

    public static class Property {

	private String key;

	private String value;

	public Property(String key, String value) {
	    this.key = key;
	    this.value = value;
	}

	public String getKey() {
	    return key;
	}

	public String getValue() {
	    return value;
	}
    }
}