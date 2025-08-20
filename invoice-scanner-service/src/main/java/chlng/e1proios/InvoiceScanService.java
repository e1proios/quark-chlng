package chlng.e1proios;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;

@ApplicationScoped
public class InvoiceScanService {

    public boolean doesInvoiceContainBlacklistedIban(String url, String[] blacklistedIbans) {
        System.out.println("Invoice URL: " + url);
        System.out.println("Blacklisted IBANs:");
        Arrays.stream(blacklistedIbans).forEach(System.out::println);
        return false;
    }
}
