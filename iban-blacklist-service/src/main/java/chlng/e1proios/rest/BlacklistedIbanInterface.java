package chlng.e1proios.rest;

import jakarta.ws.rs.PathParam;
import org.jboss.resteasy.reactive.RestResponse;

public interface BlacklistedIbanInterface {

    RestResponse<Boolean> isIbanBlacklisted(@PathParam("iban") String iban);
    RestResponse<String[]> getBlacklistedIbans();
}
