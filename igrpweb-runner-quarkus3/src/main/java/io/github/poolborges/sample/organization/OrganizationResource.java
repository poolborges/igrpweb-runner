package io.github.poolborges.sample.resource;

import io.github.poolborges.sample.organization.Organization;
import io.github.poolborges.sample.organization.OrganizationRepository;
import io.github.poolborges.sample.organization.OrganizationCollection;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/api/v1/organization")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {
    
    private final static String RESOURCE_PATH = "/api/v1/organization";
    
    @Inject
    private OrganizationRepository orgRepository;
    

    /**
     * This is expect to return a small number of organization
     * 
     * For bit number of items is best to return a paged object or HAL based resource
     * @return 
     */
    @Operation(
        summary = "Get all organizations",
        description = "Retrieves a list of organization")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "return all organizations", content = @Content(schema = @Schema(implementation = OrganizationCollection.class)))
    })
    @GET
    public Response getAll() {
        OrganizationCollection orgs = new OrganizationCollection(orgRepository.listAll());
        return  Response.ok(orgs).build();
    }
    
    @Operation(
        summary = "Get Organization by Id",
        description = "Retrieves one organization")
    @APIResponses(value = {
            @APIResponse(responseCode = "400", description = " Oranization not found"),
            @APIResponse(responseCode = "200", description = "Organization found", content = @Content(schema = @Schema(implementation = Organization.class)))
    })
    @GET
    @Path("/{id}")
    public Response getById(final @Parameter(description = "Id of the Organization") @PathParam("id") long id) {
        
        /* orgRepository.findByIdOptional(id)
                .map(org -> Response.ok(org).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());*/
        
        return orgRepository.findByIdOptional(id)
                .map(org -> Response.ok(org).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    
    /**
     * Create a Organization 
     * 
     * Can use here hibernate-validation to validade Organization
     * 
     * @param org
     * @return 
     */
    @Operation(summary = "Create new Organization", description = "createOrganization")
    @APIResponses(value = {
            @APIResponse(responseCode = "400", description = " Oranization found"),
            @APIResponse(responseCode = "201", description = "Organization created sucessfully", content = @Content(schema = @Schema(implementation = Organization.class)))
    })
    @Transactional
    @POST
    public Response create(Organization org) {
        
        Organization organization = new Organization();
        organization.setName(org.getName());
        organization.setCountry(org.getCountry());
        //orgRepository.persist(organization);
        var persisted = true; //orgRepository.isPersistent(organization);
        
        if(persisted){
            return Response.created(URI.create(RESOURCE_PATH + "/" + organization.getId())).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    @Operation(
        summary = "Get organizations filter by country",
        description = "Retrieves a list of organization for Country")
    @APIResponse(description = "return a list of organizations")
    @GET
    @Path("/filter")
    public Response getFilterByCountry(@QueryParam("country") String country) {
        
        List<Organization> orgs = country != null ? orgRepository.findByCountry(country) : orgRepository.listAll();
        OrganizationCollection orgsModel = new OrganizationCollection(orgs);
        return  Response.ok(orgsModel).build();
    }
    
    @Operation(
        summary = "Update organization",
        description = "Update organization data")
    @APIResponses(value = {
            @APIResponse(responseCode = "404", description = " Oranization not found"),
            @APIResponse(responseCode = "204", description = "Organization update sucessfully")
    })
    @Transactional
    @PUT
    @Path("/{id}")
    public Response udpate(@PathParam("id") long id, Organization organization) {
        
        var opOrg = orgRepository.findByIdOptional(id);
        
        if(opOrg.isPresent() ){
            
            var org = opOrg.get();
            
            org.setCountry(organization.getCountry());
            org.setName(organization.getName());
            
            orgRepository.persist(org);
            
            return  Response.ok(Response.Status.NO_CONTENT).build();
        }
        return  Response.ok(Response.Status.NOT_FOUND).build();
    }
    
    @Operation(
        summary = "Delete organization",
        description = "Delete a organization by Id")
    @APIResponses(value = {
            @APIResponse(responseCode = "404", description = " Oranization not found"),
            @APIResponse(responseCode = "204", description = "Organization update sucessfully")
    })
    @Transactional
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        
        if(orgRepository.deleteById(id)){
            return  Response.ok(Response.Status.NO_CONTENT).build();
        }
        return  Response.ok(Response.Status.NOT_FOUND).build();
    }
}
