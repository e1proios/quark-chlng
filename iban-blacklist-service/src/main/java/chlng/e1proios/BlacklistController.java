package chlng.e1proios;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/blacklist")
@Authenticated
public class BlacklistController {

    @Inject
    BlacklistService blacklistService;

    @GET
    @Path("/")
    @RolesAllowed("user")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String[]> getBlacklistedIbans() {
        System.out.println("getBlacklistedIbans()");
        return RestResponse.ok(this.blacklistService.getBlacklistedIbans());
    }
}
