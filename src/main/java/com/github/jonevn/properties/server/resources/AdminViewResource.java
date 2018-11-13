package com.github.jonevn.properties.server.resources;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.jonevn.properties.server.service.GitService;
import com.github.jonevn.properties.server.service.PropertiesService;
import com.github.jonevn.properties.server.views.AdminView;

@Path("admin")
public class AdminViewResource {

    private final PropertiesService propertiesService;
    private final GitService gitService;

    public AdminViewResource(PropertiesService propertiesService, GitService gitService) {
	this.propertiesService = propertiesService;
	this.gitService = gitService;
    }

    @GET
    public AdminView getAdminView() {
	return new AdminView(propertiesService, gitService);
    }

    @POST
    @Path("update")
    public Response updateProperties() {
	propertiesService.pullAndReload();
	return Response.status(Status.MOVED_PERMANENTLY).location(URI.create("/admin")).build();
    }

    @POST
    @Path("clear")
    public Response clearProperties() {
	propertiesService.clear();

	return Response.status(Status.MOVED_PERMANENTLY).location(URI.create("/admin")).build();
    }

    @POST
    @Path("branch/{name}")
    public Response changeBranch(@PathParam("name") String name) {
	gitService.checkout(name);
	propertiesService.clear();
	propertiesService.update();
	return Response.status(Status.MOVED_PERMANENTLY).location(URI.create("/admin")).build();
    }
}