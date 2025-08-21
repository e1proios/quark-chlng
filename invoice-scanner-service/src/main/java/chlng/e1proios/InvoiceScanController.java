package chlng.e1proios;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

import chlng.e1proios.client.BlacklistClient;

import java.io.IOException;

@Path("api/scan")
public class InvoiceScanController {

    public record ScanPayload(String url) {}

    @Inject
    @RestClient
    BlacklistClient blacklistClient;

    @Inject
    InvoiceScanService invoiceScanService;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public RestResponse<String> scan(ScanPayload data) {
        try (RestResponse<String[]> res = this.blacklistClient.getBlacklistedIbans()) {
            if (res.getStatus() != 200) {
                System.err.println("Status code: " + res.getStatus());
                return RestResponse.notFound();
            } else {
                String[] ibans = res.readEntity(new GenericType<>() {});

                try {
                    var invoiceDirty = this.invoiceScanService.doesInvoiceContainBlacklistedIban(data.url(), ibans);
                    return RestResponse.ok("Invoice dirty: " + invoiceDirty);
                } catch (IOException ioe) {
                    System.err.println("Invoice scan controller - inner exception: " + ioe.getMessage());
                    return RestResponse.notFound();
                }
            }
        } catch (Exception e) {
            System.err.println("Invoice scan controller - outer exception: " + e.getMessage());
            return RestResponse.notFound();
        }
    }
}
