package com.restapi.tests.jira;

import com.restapi.utils.ConfigReader;
import main.java.com.restapi.utils.PayloadReview;
import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

@Log4j2
public class CreateBugAddAttachmentInJira {

    private String authToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("jira.baseUrl");
        authToken = System.getenv("JIRA_AUTH_TOKEN");
        if (authToken == null) {
            throw new RuntimeException("JIRA_AUTH_TOKEN environment variable must be set");
        }
    }

    @Test
    public void createBugAndAddAttachment() {
        String issueId = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authToken)
                .body(PayloadReview.createBug())
                .log().all()
                .post("rest/api/3/issue").then().log().all().assertThat().statusCode(201).contentType("application/json")
                .extract().response().jsonPath().getString("id");
        Assert.assertNotNull(issueId, "Issue ID should not be null");
        log.info(issueId);

        given()
                .pathParam("key", issueId)
                .header("X-Atlassian-Token", "no-check")
                .header("Authorization", "Basic " + authToken)
                .multiPart("file", new File("testdata/AddAttachment.png")).log().all()
                .post("rest/api/3/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
        log.info("Attachment added successfully to issue: " + issueId);
    }
}
