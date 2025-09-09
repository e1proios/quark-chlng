package chlng.e1proios.client;

import io.quarkus.oidc.client.filter.OidcClientFilter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import chlng.e1proios.rest.BlacklistedIbanInterface;

@RegisterRestClient(configKey = "blacklist-service")
@OidcClientFilter
@Path("/api")
public interface BlacklistClient extends BlacklistedIbanInterface {

    @GET
    @Path("/blacklist/{iban}")
    RestResponse<Boolean> isIbanBlacklisted(@PathParam("iban") String iban);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/blacklist")
    RestResponse<String[]> getBlacklistedIbans();
}
