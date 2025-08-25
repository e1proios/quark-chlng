package chlng.e1proios.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.*;

import static chlng.e1proios.InvoiceScanService.PdfInfo;
import chlng.e1proios.util.DevLogger;
import chlng.e1proios.InvoiceScanService;

@ApplicationScoped
public class MessageEater {

    public record PdfInfoMessage(String status, String message, PdfInfo data) {}

    @Inject
    InvoiceScanService scanner;

    @Inject
    DevLogger testLogger;

    @Incoming("invoice-urls")
    @Outgoing("processed-invoices")
    public PdfInfoMessage eatMessage(String pdfUrl) {
        testLogger.log("eating message, received url: " + pdfUrl);

        if (pdfUrl.isBlank()) {
            return new PdfInfoMessage("404", "not found",  new PdfInfo());
        }
        try {
            var pdfInfoMsg = this.scanner.checkPdfForBlacklistedIbans(pdfUrl);
            return new PdfInfoMessage("200", "success", pdfInfoMsg);
        } catch (Exception e) {
            return new PdfInfoMessage("500", e.getMessage(), new PdfInfo());
        }
    }
}
