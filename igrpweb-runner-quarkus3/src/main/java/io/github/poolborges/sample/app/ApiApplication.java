package io.github.poolborges.sample.app;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 *
 * @author poolb
 */
@OpenAPIDefinition(
    tags = {
            //@Tag(name="organization", description="Organization resource operations."),
            //@Tag(name="sample", description="Operations related to sample resource")
    },
    info = @Info(
        title="IGRPweb API",
        version = "3.0.0",
        contact = @Contact(
            name = "KriolOS API Support",
            url = "poolborges.github.io",
            email = "poolborges@example.com"),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class ApiApplication extends Application {
}