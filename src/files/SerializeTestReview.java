package files;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import Lombok.AddPlace;
import Lombok.Location;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

@Log4j2
public class SerializeTestReview {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
    }

    @Test
    public void addPlaceTest() {
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
        JsonPath jsonPath = given().log().all().queryParam("key", "qaclick123")
                .body(p)
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).extract().jsonPath();

        String status = jsonPath.getString("status");
        String placeId = jsonPath.getString("place_id");

        log.info(jsonPath.prettify());

        Assert.assertEquals(status, "OK", "Status should be OK");
        Assert.assertNotNull(placeId, "place_id should not be null");
    }

}
