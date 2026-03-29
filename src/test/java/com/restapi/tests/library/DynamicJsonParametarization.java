package com.restapi.tests.library;

import com.restapi.utils.ConfigReader;
import com.restapi.utils.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJsonParametarization {

    private static final Logger log = LogManager.getLogger(DynamicJsonParametarization.class);

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("library.baseUrl");
    }

    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle) {
        JsonPath jsonPath = given().log().all().header("Content-Type", "application/json")
                .body(Payload.addBook(isbn, aisle))
                .when()
                .post("/Library/Addbook.php")
                .then().log().all().statusCode(200)
                .extract().response().jsonPath();
        String id = jsonPath.getString("ID");
        log.info("Book ID: {}", id);

        Assert.assertNotNull(id, "ID should not be null");
        Assert.assertEquals(id, isbn + aisle, "ID should match isbn + aisle");
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
