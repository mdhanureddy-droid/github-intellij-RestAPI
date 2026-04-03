package main.java.com.restapi.utils;

import com.restapi.pojo.*;

import java.util.List;
import java.util.Map;

public class PayloadReview {

    public static AddPlace addPlace() {
        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);

        return AddPlace.builder()
                .location(location)
                .accuracy(50)
                .name("Frontline house")
                .phoneNumber("(+91) 983 893 3937")
                .address("29, side layout, cohen 09")
                .types(List.of("shoe park", "shop"))
                .website("http://google.com")
                .language("French-IN")
                .build();
    }

    public static CoursePricePayload coursePrice() {
        return CoursePricePayload.builder()
                .dashboard(CoursePriceDashboard.builder()
                        .purchaseAmount(910)
                        .website("rahulshettyacademy.com")
                        .build())
                .courses(List.of(
                        CoursePriceCourse.builder().title("Selenium Python").price(50).copies(6).build(),
                        CoursePriceCourse.builder().title("Cypress").price(40).copies(4).build(),
                        CoursePriceCourse.builder().title("RPA").price(45).copies(10).build()))
                .build();
    }

    public static JiraBugPayload createBug() {
        return JiraBugPayload.builder()
                .fields(JiraBugPayload.Fields.builder()
                        .project(JiraBugPayload.Project.builder().key("DEV").build())
                        .summary("REST create bug 1.")
                        .description(JiraBugPayload.Description.builder()
                                .type("doc")
                                .version(1)
                                .content(List.of(JiraBugPayload.Content.builder()
                                        .type("paragraph")
                                        .content(List.of(Map.of(
                                                "type", "text",
                                                "text", "Creating of an issue using project keys and issue type names using the REST API")))
                                        .build()))
                                .build())
                        .issuetype(JiraBugPayload.IssueType.builder().name("Bug").build())
                        .build())
                .build();
    }

    public static AddBook addBook(String isbn, String aisle) {
        return AddBook.builder()
                .name("Learn Appium Automation with Java")
                .isbn(isbn)
                .aisle(aisle)
                .author("Dhanu")
                .build();
    }
}
