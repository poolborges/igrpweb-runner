package io.github.igrpweb.runner.quarkus.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/")
public class IndexController {

    @GET
    public Response index() {
        return Response.seeOther(URI.create("/app?from=RedirectorQuarkus")).build();
    }

    @GET
    @Path("/index.jsp")
    public Response indexJSP() {
        return Response.seeOther(URI.create("/app?redirect_from=RedirectorQuarkus")).build();
    }
}
