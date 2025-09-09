package chlng.e1proios;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.GenericType;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import javax.net.ssl.SSLHandshakeException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

import chlng.e1proios.util.DevLogger;
import chlng.e1proios.client.BlacklistClient;

@ApplicationScoped
public class InvoiceScanService {

    private final HttpClient http;

    public record PdfInfo(String srcUrl, boolean dirty, String firstFound, int page) {
        public PdfInfo() {
            this("", false, "", 0);
        }
    }

    @Inject
    @RestClient
    BlacklistClient blacklistClient;

    @Inject
    DevLogger devLogger;

    {
        this.http = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(15))
            .build();
    }

    public PdfInfo checkPdfForBlacklistedIbans(String pdfUrl) throws Exception {
        HttpResponse<InputStream> pdfResponse = this.fetchPDF(pdfUrl);

        if (pdfResponse.statusCode() != 200) {
            throw new Exception("PDF fetch failed: " + pdfResponse.statusCode());
        } else {
            InputStream pdfStream = pdfResponse.body();
            String[] blacklistedIbans = this.getBlacklistedIbans();

            try (PDDocument pdfDoc =
                Loader.loadPDF(RandomAccessReadBuffer.createBufferFromStream(pdfStream))
            ) {
                return this.processPdf(pdfUrl, pdfDoc, blacklistedIbans);
            } catch (IOException ioe) {
                throw new Exception("Error reading PDF: " + ioe.getMessage());
            }
        }
    }

    private String[] getBlacklistedIbans() throws Exception {
        RestResponse<String[]> blacklistResponse = this.blacklistClient.getBlacklistedIbans();

        if (blacklistResponse.getStatus() != 200) {
            throw new Exception("IBAN blacklist fetch failed: " + blacklistResponse.getStatus());
        }
        return blacklistResponse.readEntity(new GenericType<>() {});
    }

    private PdfInfo processPdf (
        String srcUrl,
        PDDocument pdfDoc,
        String[] blacklistedIbans
    ) throws IOException {
        var totalPages = pdfDoc.getNumberOfPages();
        PdfInfo ret = new PdfInfo();
        int pageNum;

        this.devLogger.log("PDF has " + totalPages + " pages");

        PDFTextStripper stripper = new PDFTextStripper();

        for (pageNum = 1; pageNum <= totalPages; pageNum++) {
            stripper.setStartPage(pageNum);
            stripper.setEndPage(pageNum);

            StringWriter writer = new StringWriter();
            stripper.writeText(pdfDoc, writer);

            String pageText = writer.toString().replaceAll("\\s+", "");

            Optional<String> res = Arrays.stream(blacklistedIbans)
                .filter(pageText::contains)
                .findFirst();

            if (res.isPresent()) {
                ret =  new PdfInfo(srcUrl,true, res.get(), pageNum);
                break;
            }
        }
        this.devLogger.log("Total pages checked: " + pageNum);
        return ret;
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
