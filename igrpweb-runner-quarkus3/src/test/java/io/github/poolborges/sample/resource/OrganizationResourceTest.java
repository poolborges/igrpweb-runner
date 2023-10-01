package io.github.poolborges.sample.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import jakarta.ws.rs.core.MediaType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

@QuarkusTest
public class OrganizationResourceTest {

    //@Test
    public void testGetAllEndpoint() {
        given()
                .when().get("/api/v1/organization")
                .then()
                .statusCode(200)
                .body("$.size()", is(3),
                        "name", containsInAnyOrder("Byggdagboken", "Organization 1", "Organization 3"),
                        "country", containsInAnyOrder("Sweden", "Portugal", "Cabo Verde"));
    }

    
    //@Test
    public void createEndpoint(){

        given()
                .body("{\"name\": \"WHO\", \"country\": \"Switzerland \"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/api/v1/organization")
                .then()
                .statusCode(200);
    }
}