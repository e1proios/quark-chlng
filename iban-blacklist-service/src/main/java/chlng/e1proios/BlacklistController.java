package chlng.e1proios;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api")
@Authenticated
public class BlacklistController {

    @Inject
    BlacklistService blacklistService;

    @GET
    @Path("/blacklist")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String[]> getBlacklistedIbans() {
        System.out.println("getBlacklistedIbans()");
        return RestResponse.ok(this.blacklistService.getBlacklistedIbans());
    }
}
