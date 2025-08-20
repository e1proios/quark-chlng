package chlng.e1proios.test;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.GenericType;
import java.util.Arrays;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

import chlng.e1proios.client.BlacklistClient;

// @QuarkusMain
public class ConsumeResource /* implements QuarkusApplication */{

/*
    @Inject
    @RestClient
    BlacklistClient blacklistClient;

    @Override
    public int run(String... args) {
        try (RestResponse<String[]> res = this.blacklistClient.getBlacklistedIbans()) {
            if (res.getStatus() != 200) {
                System.err.println("Status code: " + res.getStatus());
            } else {
                String[] ibans = res.readEntity(new GenericType<>() {});
                System.out.println("Blacklisted IBANs:");
                Arrays.stream(ibans).forEach(System.out::println);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
*/
}
