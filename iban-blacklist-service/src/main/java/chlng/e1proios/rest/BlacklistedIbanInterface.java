package chlng.e1proios.rest;

import org.jboss.resteasy.reactive.RestResponse;

public interface BlacklistedIbanInterface {
    RestResponse<String[]> getBlacklistedIbans();
}
