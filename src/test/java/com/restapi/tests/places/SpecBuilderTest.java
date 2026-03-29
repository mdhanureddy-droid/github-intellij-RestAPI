package com.restapi.tests.places;

import com.restapi.pojo.AddPlace;
import com.restapi.pojo.Location;
import com.restapi.utils.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

@Log4j2
public class SpecBuilderTest {

    private static final String BASE_URL = ConfigReader.getBaseUrl();
    private static final String API_KEY = ConfigReader.getApiKey();

    private RequestSpecification reqSpec;
    private ResponseSpecification resSpec;

    @BeforeClass
    public void setup() {
        reqSpec = new RequestSpecBuilder().setBaseUri(BASE_URL)
                .addQueryParam("key", API_KEY)
                .setContentType(ContentType.JSON).build();
        resSpec = new ResponseSpecBuilder().expectStatusCode(200)
                .expectContentType(ContentType.JSON).build();
    }

    @Test
    public void addPlace() {
        Location l = new Location();
        l.setLat(-38.383494);
        l.setLng(33.427362);

        AddPlace p = AddPlace.builder()
                .accuracy(50)
                .address("29, side layout, cohen 09")
                .language("French-IN")
                .phoneNumber("(+91) 983 893 3937")
                .website("https://rahulshettyacademy.com")
                .name("Frontline house")
                .types(List.of("shoe park", "shop"))
                .location(l)
                .build();

        JsonPath jsonPath = given().spec(reqSpec)
                .body(p)
                .when().post("/maps/api/place/add/json")
                .then().spec(resSpec).extract().response().jsonPath();

        String status = jsonPath.getString("status");
        String placeId = jsonPath.getString("place_id");
        log.info("Place ID: {}", placeId);

        Assert.assertEquals(status, "OK", "Status should be OK");
        Assert.assertNotNull(placeId, "Place ID should not be null");
        Assert.assertFalse(placeId.isEmpty(), "Place ID should not be empty");
    }
}
