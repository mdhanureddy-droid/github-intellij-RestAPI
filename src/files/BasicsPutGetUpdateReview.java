package files;

import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Log4j2
public class BasicsPutGetUpdateReview {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void verifyAddAndUpdatePlace() {
        String placeId = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(Payload.addPlace()).when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().path("place_id");

        Assert.assertNotNull(placeId, "place_id should not be null");
        log.info(placeId);

        String newAddress = "Summer Walk, Africa";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\r\n"
                        + "\"place_id\":\"" + placeId + "\",\r\n"
                        + "\"address\":\"" + newAddress + "\",\r\n"
                        + "\"key\":\"qaclick123\"\r\n"
                        + "}")
                .when().put("maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
        String actualAddress = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().path("address");
        Assert.assertNotNull(actualAddress, "address should not be null");
        Assert.assertFalse(actualAddress.isEmpty(), "address should not be empty");
        log.info(actualAddress);
        Assert.assertEquals(actualAddress, "Summer Walk, Africa");
    }

}
