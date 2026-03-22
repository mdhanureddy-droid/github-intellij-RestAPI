package files;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@Log4j2
public class StaticFilePostMethod {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
    }

    private String placeId;

    @Test(priority = 1)
    public void addPlace() throws IOException {
        Response response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("testdata/addPlace.json")))).when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().response();

        log.info(response.asString());
        placeId = response.jsonPath().getString("place_id");
        Assert.assertNotNull(placeId, "Place ID should not be null");
        log.info(placeId);
    }

    @Test(priority = 2, dependsOnMethods = "addPlace")
    public void updatePlace() {
        String newAddress = "Summer Walk, Africa";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\r\n"
                        + "\"place_id\":\"" + placeId + "\",\r\n"
                        + "\"address\":\"" + newAddress + "\",\r\n"
                        + "\"key\":\"qaclick123\"\r\n"
                        + "}")
                .when().put("maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
    }

    @Test(priority = 3, dependsOnMethods = "updatePlace")
    public void verifyUpdatedAddress() {
        String actualAddress = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200)
                .extract().response().jsonPath().getString("address");

        log.info(actualAddress);
        Assert.assertEquals(actualAddress, "Summer Walk, Africa", "Updated address should match expected value");
    }

}
