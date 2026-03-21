package files;

import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

@Log4j2
public class CreateBugAddAttachmentInJiraReview {

    private String issueId;
    private String authToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://mdhanureddy.atlassian.net/";
        authToken = System.getenv("JIRA_AUTH_TOKEN");
        if (authToken == null) {
            throw new RuntimeException("JIRA_AUTH_TOKEN environment variable must be set");
        }
    }

    @Test(priority = 1)
    public void createBugInJira() {
        issueId = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authToken)
                .body("{\r\n"
                        + "    \"fields\": {\r\n"
                        + "       \"project\":\r\n"
                        + "       {\r\n"
                        + "          \"key\": \"DEV\"\r\n"
                        + "       },\r\n"
                        + "       \"summary\": \"REST create bug 1.\",\r\n"
                        + "       \"description\": {\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"type\":\"text\",\"text\":\"Creating of an issue using project keys and issue type names using the REST API\"}]}]},\r\n"
                        + "       \"issuetype\": {\r\n"
                        + "          \"name\": \"Bug\"\r\n"
                        + "       }\r\n"
                        + "   }\r\n"
                        + "}")
                .log().all()
                .post("rest/api/3/issue").then().log().all().assertThat().statusCode(201).contentType("application/json")
                .extract().response().jsonPath().getString("id");
        Assert.assertNotNull(issueId, "Issue ID should not be null");
        log.info(issueId);
    }

    @Test(priority = 2, dependsOnMethods = "createBugInJira")
    public void addAttachmentToIssue() {
        given()
                .pathParam("key", issueId)
                .header("X-Atlassian-Token", "no-check")
                .header("Authorization", "Basic " + authToken)
                .multiPart("file", new File("testdata/AddAttachment.png")).log().all()
                .post("rest/api/3/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
        log.info("Attachment added successfully to issue: " + issueId);
    }

}
