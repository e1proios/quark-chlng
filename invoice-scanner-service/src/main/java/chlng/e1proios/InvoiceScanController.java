package chlng.e1proios;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

import chlng.e1proios.client.BlacklistClient;
import chlng.e1proios.util.DevLogger;

@Path("api/scan")
public class InvoiceScanController {

    public record ScanRequestPayload(String url) {}

    @Inject
    @RestClient
    BlacklistClient blacklistClient;

    @Inject
    InvoiceScanService invoiceScanService;

    @Inject
    DevLogger testLogger;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<InvoiceScanService.PdfInfo> scan(ScanRequestPayload data) {
        try (RestResponse<String[]> res = this.blacklistClient.getBlacklistedIbans()) {
            if (res.getStatus() != 200) {
                this.testLogger.log("Status code: " + res.getStatus(), true);
                return RestResponse.notFound();
            } else {
                String[] ibans = res.readEntity(new GenericType<>() {});

                try {
                    var invoiceInfo = this.invoiceScanService.checkPdfForBlacklistedIbans(data.url(), ibans);
                    return RestResponse.ok(invoiceInfo);
                } catch (IOException ioe) {
                    this.testLogger.log("Invoice scan controller - inner exception: " + ioe.getMessage(), true);
                    return RestResponse.notFound();
                }
            }
        } catch (Exception e) {
            this.testLogger.log("Invoice scan controller - outer exception: " + e.getMessage(), true);
            return RestResponse.notFound();
        }
    }
}
