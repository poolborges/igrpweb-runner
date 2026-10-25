package io.github.igrpweb.runner.quarkus.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class CustomErrorController {

    @GET
    @Path("/error")
    @Produces(MediaType.TEXT_HTML)
    public Response error() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<html><body><h1>Error</h1></body></html>")
                .build();
    }

    @GET
    @Path("/error.jsp")
    @Produces(MediaType.TEXT_HTML)
    public Response errorJSP() {
        return error();
    }
}
