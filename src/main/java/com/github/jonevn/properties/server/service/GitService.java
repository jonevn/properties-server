package com.github.jonevn.properties.server.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.PullResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jonevn.properties.server.config.GitConfiguration;

public class GitService {

    private static final Logger logger = LoggerFactory.getLogger(GitService.class);

    private Git git;

    private final GitConfiguration gitConfiguration;

    private File localDirectory;

    public GitService(GitConfiguration gitConfiguration) {
	this.gitConfiguration = gitConfiguration;
	this.localDirectory = Paths.get(gitConfiguration.getPathToCloneTo()).resolve("properties").toFile();

	cloneRepository();
    }

    public File localDirectory() {
	return localDirectory;
    }

    public void cloneRepository() {
	if (git == null && localDirectory.isDirectory()) {
	    try {
		this.git = Git.open(localDirectory);
		logger.info("Opened GIT-repo at " + localDirectory.getAbsolutePath());
	    } catch (Exception e) {
		throw new PropertiesServerException("Failed to open properties repoository: " + e.getMessage(), e);
	    }
	} else {
	    try {
		this.git = Git.cloneRepository().setDirectory(localDirectory).setBranch(gitConfiguration.getBranch())
			.setURI(gitConfiguration.getRepositoryUrl()).call();
		logger.info(
			"Cloned " + gitConfiguration.getRepositoryUrl() + " to " + localDirectory.getAbsolutePath());
	    } catch (Exception e) {
		throw new PropertiesServerException("Failed to clone git repository: " + e.getMessage(), e);
	    }
	}
    }

    public String currentBranch() {
	try {
	    return git.getRepository().getBranch();
	} catch (IOException e) {
	    return "Could not read current branch";
	}
    }

    public Set<Branch> availableBranches(ListMode listMode) {
	try {
	    return git.branchList().setListMode(listMode).call().stream().map(ref -> ref.getName())
		    .map(name -> new Branch(name)).collect(Collectors.toSet());
	} catch (Exception e) {
	    logger.error("Failed to read branches: " + e.getMessage(), e);
	    return Collections.emptySet();
	}
    }

    public void fetch() {
	try {
	    git.fetch().call();
	} catch (Exception e) {
	    throw new PropertiesServerException("Failed to execute fetch: " + e.getMessage(), e);
	}
    }

    public void checkout(String branch) {
	try {
	    if (isLocalBranch(branch)) {
		git.checkout().setName(branch).call();
	    } else {
		git.checkout().setName(branch).setCreateBranch(true).setUpstreamMode(SetupUpstreamMode.TRACK).call();
	    }
	} catch (Exception e) {
	    throw new PropertiesServerException(
		    "Failed to execute checkout of branch " + branch + ": " + e.getMessage(), e);
	}
    }

    private boolean isLocalBranch(String branchName) throws Exception {
	return git.branchList().call().stream().map(branchRef -> new Branch(branchRef.getName()))
		.filter(branch -> branch.getDisplayName().equals(branchName)).findFirst().isPresent();
    }

    public void pull() {
	PullResult pullResult;
	try {
	    pullResult = git.pull().call();
	} catch (Exception e) {
	    throw new PropertiesServerException("Failed to execute pull: " + e.getMessage(), e);
	}
	if (!pullResult.isSuccessful()) {
	    throw new PropertiesServerException("Failed to execute pull: " + pullResult.getFetchResult().getMessages());
	}
    }

}
