package chlng.e1proios;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

import java.util.Arrays;

@QuarkusMain
public class EatResource implements QuarkusApplication {

    @Inject
    BlacklistService blacklistSvc;

    @Override
    public int run(String... args) {
        var ibans = this.blacklistSvc.getBlacklistedIbans();

        System.out.println("Blacklisted IBANs:");
        Arrays.stream(ibans).forEach(System.out::println);

        return 0;
    }
}
