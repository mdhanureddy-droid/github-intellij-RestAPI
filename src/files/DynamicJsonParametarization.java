package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJsonParametarization {
	
@Test(dataProvider="BooksData")
public void addBook(String isbn,String aisle)
{
	RestAssured.baseURI="http://216.10.245.166";
	String response = given().log().all().header("Content-Type","application/json").
	body(payload.Addbook(isbn,aisle)).
	when()
	.post("/Library/Addbook.php")
	.then().log().all().statusCode(200)
	.extract().response().asString();
	JsonPath js = ReUsableMethods.rawToJson(response);
	String id = js.get("ID");
	System.out.println(id);
	
}

@DataProvider(name="BooksData")

public Object[][] getData()
{
	//array = collection of elements 
	//multidimentional array = collection of arrays
	return new Object[][] {{"hsdjhddfj","1234"},{"adsfdsa","4567"},{"sdhoiodsf","46274"}};
	
}

}
