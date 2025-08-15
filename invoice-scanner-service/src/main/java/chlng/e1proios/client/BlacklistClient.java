package chlng.e1proios.client;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import chlng.e1proios.rest.BlacklistedIbanInterface;

@RegisterRestClient(configKey = "blacklist-service")
@Path("/api/blacklist")
public interface BlacklistClient extends BlacklistedIbanInterface {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    RestResponse<String[]> getBlacklistedIbans();
}
