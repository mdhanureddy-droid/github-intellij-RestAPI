
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import java.io.File;


public class CreateBugAddAttachmentinJira {
	public static void main(String[] args) {		// TODO Auto-generated method stub
			RestAssured.baseURI="https://mdhanureddy.atlassian.net/";
			String createIssueResponse 	= given()		
			.header("Content-Type","application/json")		
			.header("Authorization","Basic bWRoYW51LnJlZGR5QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBvSlpLS3psb1RrdlhhZ0NtbURkMTlzX2pNUDBlQUJ1SDZnODhwaG5PSjVRaUw2Y1ZFX0hJQm1HRFB1NEN5dEczc0dDSGx4MjlFelZMb2F5eGZCTGpjUDFRbFBMR2pZdUtwbkZPTmNLcVdWQU1YMW1OZWx4anc0bFNTT3Q3ZE1WUS1lUXRxczZVdzRWMmVTZU1kVXgzYUFfSHhhWmxKQWxnUVNvd2RxSjJPWUU9NjMxNUFFMkU=")
			.body("{\r\n"
					+ "    \"fields\": {\r\n"
					+ "       \"project\":\r\n"
					+ "       {\r\n"
					+ "          \"key\": \"DEV\"\r\n"
					+ "       },\r\n"
					+ "       \"summary\": \"REST create bug 1.\",\r\n"
					+ "       \"description\": [\"Creating of an issue using project keys and issue type names using the REST API\"],\r\n"
					+ "       \"issuetype\": {\r\n"
					+ "          \"name\": \"Bug\"\r\n"
					+ "       }\r\n"
					+ "   }\r\n"
					+ "}")		
			.log().all()
			.post("rest/api/3/issue").then().log().all().assertThat().statusCode(201).contentType("application/json")
			.extract().response().asString();
			JsonPath js = new JsonPath(createIssueResponse);
			String issueId = js.getString("id");
			System.out.println(issueId);
			given()
			.pathParam("key", issueId)
			.header("X-Atlassian-Token","no-check")
			.header("Authorization","Basic bWRoYW51LnJlZGR5QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBvSlpLS3psb1RrdlhhZ0NtbURkMTlzX2pNUDBlQUJ1SDZnODhwaG5PSjVRaUw2Y1ZFX0hJQm1HRFB1NEN5dEczc0dDSGx4MjlFelZMb2F5eGZCTGpjUDFRbFBMR2pZdUtwbkZPTmNLcVdWQU1YMW1OZWx4anc0bFNTT3Q3ZE1WUS1lUXRxczZVdzRWMmVTZU1kVXgzYUFfSHhhWmxKQWxnUVNvd2RxSjJPWUU9NjMxNUFFMkU=")
			.multiPart("file",new File("\\Users\\dhanunjaya.r\\Desktop\\AddAttachment.png")).log().all()
			.post("rest/api/3/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);			
			}
}
