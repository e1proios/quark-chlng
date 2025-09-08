package chlng.e1proios;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class InvoiceScanControllerTest {

    @InjectMock
    InvoiceScanService scanService;

    @Test
    @TestSecurity(authorizationEnabled = false)
    public void testPingEndpoint() {
        Response ping = RestAssured.given()
            .contentType("text/plain")
            .get("/api/ping");

        Assertions.assertEquals(200, ping.getStatusCode());
        Assertions.assertEquals("invoice scanner is alive and reachable", ping.getBody().asString());
    }

    @Test
    public void testScanEndpoint() {
        try {
            Mockito.when(
                scanService.checkPdfForBlacklistedIbans(any(String.class))
            ).thenReturn(
                new InvoiceScanService.PdfInfo()
            );
        } catch (Exception e) {
            // do nothing
        }
    }
}
