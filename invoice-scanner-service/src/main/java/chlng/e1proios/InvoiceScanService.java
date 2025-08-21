package chlng.e1proios;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.*;
import java.time.Duration;
import javax.net.ssl.SSLHandshakeException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@ApplicationScoped
public class InvoiceScanService {

    private final HttpClient http;

    {
        this.http = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(15))
            .build();
    }

    public boolean doesInvoiceContainBlacklistedIban(
        String pdfUrl,
        String[] blacklistedIbans
    ) throws Exception {
        HttpResponse<InputStream> res = this.fetchPDF(pdfUrl);

        if (res.statusCode() != 200) {
            System.err.println("Failed to fetch PDF: " + res.statusCode());
            return false;
        } else {
            System.out.println("PDF fetched successfully");

            try (InputStream pdfStream = res.body();
                 PDDocument document = Loader.loadPDF(RandomAccessReadBuffer.createBufferFromStream(pdfStream))
            ) {
                System.out.println("Processing PDF");
                PDFTextStripper stripper = new PDFTextStripper();
                System.out.println(stripper.getText(document));
                return false;
            }
        }
    }

    private HttpResponse<InputStream> fetchPDF(String pdfUrl) throws Exception {
        HttpRequest pdfRequest = HttpRequest.newBuilder()
            .uri(URI.create(pdfUrl))
            .timeout(Duration.ofSeconds(30))
            .GET()
            .build();

        try {
            return this.http.send(pdfRequest, HttpResponse.BodyHandlers.ofInputStream());
        } catch (HttpTimeoutException e) {
            throw new Exception("Request timed out: " + e.getMessage(), e);
        } catch (UnknownHostException e) {
            throw new Exception("Unknown host: " + e.getMessage(), e);
        } catch (ConnectException e) {
            throw new Exception("Connection refused: " + e.getMessage(), e);
        } catch (SSLHandshakeException e) {
            throw new Exception("SSL handshake failed: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new Exception("Network error: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Exception("Request interrupted", e);
        }
    }
}
