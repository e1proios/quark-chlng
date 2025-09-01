package chlng.e1proios;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

import chlng.e1proios.util.DevLogger;

@Authenticated
@Path("api/")
public class InvoiceScanController {

    public record ScanRequestPayload(String url) {}

    @Inject
    InvoiceScanService invoiceScanService;

    @Inject
    DevLogger testLogger;

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public RestResponse<String> testAuth() {
        return RestResponse.ok("invoice scanner is alive and reachable");
    }

    @POST
    @Path("/scan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<InvoiceScanService.PdfInfo> scan(ScanRequestPayload data) {
        if (data.url.isBlank()) {
            this.testLogger.log("Invalid URL", true);
            return RestResponse.notFound();
        }

        try {
            // var pdfInfo = this.invoiceScanService.checkPdfForBlacklistedIbans(data.url());
            // return RestResponse.ok(pdfInfo);
            return RestResponse.ok(new InvoiceScanService.PdfInfo(data.url, false, "", -1));
        } catch (Exception e) {
            this.testLogger.log("Invoice scan controller: " + e.getMessage(), true);
            return RestResponse.status(500, e.getMessage());
        }
    }
}
