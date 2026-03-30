package com.restapi.tests.places;

import com.restapi.listeners.TestListener;
import com.restapi.utils.ConfigReader;
import com.restapi.utils.ExcelDataReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Log4j2
@Listeners(TestListener.class)
public class StaticFilePostMethodListeners {

    private static final String DEFAULT_KEY = ConfigReader.getApiKey();
    private static final String DEFAULT_ADDRESS = "Summer Walk, Africa";

    private String placeId;
    private Map<String, String> testData;

    @BeforeClass
    public void setup() throws IOException {
        RestAssured.baseURI = ConfigReader.getBaseUrl();
        RestAssured.useRelaxedHTTPSValidation();

        Map<String, String> excelData = new ExcelDataReader().getDataAsMap("AddPlace", "testdata");
        testData = new HashMap<>();
        testData.put("Key", validOrDefault(excelData.get("Key"), DEFAULT_KEY));
        testData.put("Address", validOrDefault(excelData.get("Address"), DEFAULT_ADDRESS));
    }

    private String validOrDefault(String value, String defaultValue) {
        return (value != null && !value.isEmpty() && !"0".equals(value)) ? value : defaultValue;
    }

    @Test
    public void addPlace() throws IOException {
        String key = testData.get("Key");
        Response response = given().log().all().queryParam("key", key).header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("testdata/addPlace.json"))))
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().response();

        log.info(response.asString());
        placeId = response.jsonPath().getString("place_id");
        Assert.assertNotNull(placeId, "Place ID should not be null");
        log.info(placeId);
    }

    @Test(dependsOnMethods = "addPlace")
    public void updatePlace() {
        String key = testData.get("Key");
        String newAddress = testData.get("Address");
        given().log().all().queryParam("key", key).header("Content-Type", "application/json")
                .body("{\r\n"
                        + "\"place_id\":\"" + placeId + "\",\r\n"
                        + "\"address\":\"" + newAddress + "\",\r\n"
                        + "\"key\":\"" + key + "\"\r\n"
                        + "}")
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
    }

    @Test(dependsOnMethods = "updatePlace")
    public void verifyUpdatedAddress() {
        String key = testData.get("Key");
        String expectedAddress = testData.get("Address");
        String actualAddress = given().log().all().queryParam("key", key)
                .queryParam("place_id", placeId)
                .when().get("/maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200)
                .extract().response().jsonPath().getString("address");

        log.info(actualAddress);
        Assert.assertEquals(actualAddress, expectedAddress, "Updated address should match expected value");
    }

}
