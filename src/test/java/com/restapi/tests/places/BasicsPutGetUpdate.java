package com.restapi.tests.places;

import com.restapi.utils.ConfigReader;
import com.restapi.utils.Payload;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Log4j2
public class BasicsPutGetUpdate {

    private static final String BASE_URL = ConfigReader.getBaseUrl();
    private static final String API_KEY = ConfigReader.getApiKey();
    private static final String PARAM_KEY = "key";
    private static final String PARAM_PLACE_ID = "place_id";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String PLACE_API_BASE = "maps/api/place/";

    private RequestSpecification requestSpec;
    private ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.useRelaxedHTTPSValidation();

        requestSpec = new RequestSpecBuilder()
                .addQueryParam(PARAM_KEY, API_KEY)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBaseUri(BASE_URL)
                .build().log().all();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    @Test
    public void verifyAddAndUpdatePlace() {
        String placeId = given().spec(requestSpec)
                .body(Payload.addPlace()).when().post(PLACE_API_BASE + "add/json")
                .then().spec(responseSpec).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().path(PARAM_PLACE_ID);

        Assert.assertNotNull(placeId, "place_id should not be null");
        log.info(placeId);

        String newAddress = "Summer Walk, Africa";
        given().spec(requestSpec)
                .body("{\r\n"
                        + "\"place_id\":\"" + placeId + "\",\r\n"
                        + "\"address\":\"" + newAddress + "\",\r\n"
                        + "\"key\":\"" + API_KEY + "\"\r\n"
                        + "}")
                .when().put(PLACE_API_BASE + "update/json")
                .then().spec(responseSpec).log().all().body("msg", equalTo("Address successfully updated"));
        String actualAddress = given().spec(requestSpec)
                .queryParam(PARAM_PLACE_ID, placeId)
                .when().get(PLACE_API_BASE + "get/json")
                .then().spec(responseSpec).log().all().extract().path("address");
        Assert.assertNotNull(actualAddress, "address should not be null");
        Assert.assertFalse(actualAddress.isEmpty(), "address should not be empty");
        log.info(actualAddress);
        Assert.assertEquals(actualAddress, "Summer Walk, Africa");
    }

}
