package chlng.e1proios;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    @TestSecurity(authorizationEnabled = false)
    public void testScanEndpointEmptyUrl() {
        var scanRequestPayload = new InvoiceScanController.ScanRequestPayload("");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(scanRequestPayload)
            .when()
            .post("/api/scan")
            .then()
            .statusCode(404);
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    public void testScanEndpointError() throws Exception {
        var scanRequestPayload = new InvoiceScanController.ScanRequestPayload("ERROR_URL");

        Mockito.when(
            scanService.checkPdfForBlacklistedIbans("ERROR_URL")
        ).thenThrow(
            new RuntimeException("simulated exception message")
        );

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(scanRequestPayload)
            .when()
            .post("api/scan")
            .then()
            .statusCode(500);
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    public void testScanEndpointSuccess() throws Exception {
        var scanRequestPayload = new InvoiceScanController.ScanRequestPayload("SUCCESS_URL");

        Mockito.when(
            scanService.checkPdfForBlacklistedIbans("SUCCESS_URL")
        ).thenReturn(
            new InvoiceScanService.PdfInfo("success_url", false, "", -1)
        );

        Response scanSuccess = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(scanRequestPayload)
            .when()
            .post("api/scan");

        Assertions.assertEquals(200, scanSuccess.getStatusCode());

        var scanSuccessBody = scanSuccess.as(InvoiceScanService.PdfInfo.class);
        Assertions.assertEquals("success_url", scanSuccessBody.srcUrl());
    }
}
