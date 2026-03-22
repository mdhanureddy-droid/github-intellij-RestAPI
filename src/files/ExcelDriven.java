package files;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

@Log4j2
public class ExcelDriven {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://216.10.245.166";
    }

    @Test(dataProvider = "BooksData")
    public void addBook(String name, String isbn, String aisle, String author) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("isbn", isbn);
        map.put("aisle", aisle);
        map.put("author", author);

        JsonPath jsonPath = given()
                .header("Content-Type", "application/json")
                .body(map)
                .when()
                .post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().jsonPath();
        String id = jsonPath.getString("ID");
        Assert.assertNotNull(id, "Book ID should not be null");
        Assert.assertFalse(id.isEmpty(), "Book ID should not be empty");
        Assert.assertEquals(id, isbn + aisle, "Book ID should match isbn + aisle");
        log.info(id);
    }

    @DataProvider(name = "BooksData")
    public Object[][] getData() throws IOException {
        DataDrivenReview d = new DataDrivenReview();
        ArrayList data = d.getData("RestAddbook", "sample");
        return new Object[][] {
            {data.get(1).toString(), data.get(2).toString(), data.get(3).toString(), data.get(4).toString()}
        };
    }

    public static String generateStringFromResource(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

}
