package chlng.e1proios;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/blacklist")
public class BlacklistController {

    @Inject
    BlacklistService blacklistService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String[]> getBlacklistedIbans() {
        System.out.println("GETTING BLACKLISTED IBANS");
        return RestResponse.ok(this.blacklistService.getBlacklistedIbans());
    }
}
