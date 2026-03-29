package com.restapi.tests.oauth;

import com.restapi.pojo.Api;
import com.restapi.pojo.GetCourse;
import com.restapi.pojo.WebAutomation;
import com.restapi.utils.ConfigReader;
import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@Log4j2
public class Oauth2ClientCredentials {

    private String accessToken;
    private GetCourse gc;

    @BeforeClass
    public void setup() {
        String clientId = ConfigReader.get("oauth.clientId");
        String clientSecret = ConfigReader.get("oauth.clientSecret");

        String response = given()
                .formParams("client_id", clientId)
                .formParams("client_secret", clientSecret)
                .formParams("grant_type", "client_credentials")
                .formParams("scope", "trust")
                .when().log().all()
                .post(ConfigReader.getBaseUrl() + "/oauthapi/oauth2/resourceOwner/token").asString();

        log.info(response);
        JsonPath jsonPath = JsonPath.from(response);
        accessToken = jsonPath.getString("access_token");
        Assert.assertNotNull(accessToken, "Access token should not be null");
        gc = given()
                .queryParams("access_token", accessToken)
                .when().log().all()
                .get(ConfigReader.getBaseUrl() + "/oauthapi/getCourseDetails").as(GetCourse.class);
        Assert.assertNotNull(gc, "GetCourse response should not be null");
    }

    @Test
    public void verifyCourseDetails() {
        Assert.assertNotNull(gc.getLinkedIn(), "LinkedIn should not be null");
        Assert.assertNotNull(gc.getInstructor(), "Instructor should not be null");
        Assert.assertNotNull(gc.getCourses().getApi().get(1).getCourseTitle(), "API course title should not be null");
        log.info(gc.getLinkedIn());
        log.info(gc.getInstructor());
        log.info(gc.getCourses().getApi().get(1).getCourseTitle());
    }

    @Test
    public void verifySoapUiCoursePrice() {
        List<Api> apiCourses = gc.getCourses().getApi();
        Assert.assertFalse(apiCourses.isEmpty(), "API courses list should not be empty");
        boolean courseFound = false;
        for (int i = 0; i < apiCourses.size(); i++) {
            if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
                Assert.assertNotNull(apiCourses.get(i).getPrice(), "SoapUI course price should not be null");
                log.info(apiCourses.get(i).getPrice());
                courseFound = true;
            }
        }
        Assert.assertTrue(courseFound, "SoapUI Webservices testing course should exist");
    }

    @Test
    public void verifyWebAutomationCourseTitles() {
        String[] courseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};
        List<WebAutomation> w = gc.getCourses().getWebAutomation();
        Assert.assertNotNull(w, "Web automation courses should not be null");
        Assert.assertEquals(w.size(), courseTitles.length, "Web automation courses count mismatch");
        ArrayList<String> actualTitles = new ArrayList<String>();
        for (int j = 0; j < w.size(); j++) {
            actualTitles.add(w.get(j).getCourseTitle());
        }
        List<String> expectedList = Arrays.asList(courseTitles);
        Assert.assertEquals(actualTitles, expectedList, "Web automation course titles mismatch");
    }

}
