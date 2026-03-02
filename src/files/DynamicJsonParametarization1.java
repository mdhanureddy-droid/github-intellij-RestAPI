package files;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJsonParametarization1 {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://216.10.245.166";
    }

    @Test(dataProvider = "BooksData")

    public void addBook(String isbn, String aisle) {
        String response = given().log().all().header("Content-Type", "application/json")
                .body(Payload.addBook(isbn, aisle))
                .when()
                .post("/Library/Addbook.php")
                .then().log().all().statusCode(200)
                .extract().response().asString();
        JsonPath jsonPath = ReusableMethods.rawToJson(response);
        String id = jsonPath.get("ID");
        System.out.println(id);

        Assert.assertNotNull(id, "ID should not be null");
    }

    @DataProvider(name = "BooksData")
    public Object[][] getData() {
        return new Object[][] {
            {"testing", "12345"},
            {"testing2", "45678"},
            {"testing3", "462745"}
        };
    }

}
