package chlng.e1proios.kafka;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.*;

import static chlng.e1proios.InvoiceScanService.PdfInfo;
import chlng.e1proios.util.DevLogger;

@ApplicationScoped
public class EaterOfMessages {

    @Inject
    DevLogger testLogger;

    @Incoming("invoice-urls")
    @Outgoing("processed-invoices")
    @Blocking
    public /*PdfInfo*/ String eatMessage(String pdfUrl) throws InterruptedException {
        if (!pdfUrl.isBlank()) {
            String res = "eating message, received url: " + pdfUrl;

            this.testLogger.log(res);
//        return new PdfInfo(pdfUrl, false, "NOT AN ACTUAL IBAN", -1);
            return res;
        }
        return "not a valid message";
    }
}
