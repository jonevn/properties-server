package se.evelonn.properties.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import se.evelonn.properties.server.service.PropertiesService;

@Path("property")
public class PropertyResource {

    private final PropertiesService propertiesService;

    public PropertyResource(PropertiesService propertiesService) {
	this.propertiesService = propertiesService;
    }

    @GET
    @Path("{propertyName}")
    public Response getPropertyWithName(@PathParam("propertyName") String propertyName) {
	String property = propertiesService.getProperty(propertyName);
	if (property == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	return Response.ok(property).build();
    }
}