package files;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ComplexJsonParseReview {

    private JsonPath jsonPath;
    private List<Map<String, Object>> courses;

    @BeforeClass
    public void setup() {
        jsonPath = new JsonPath(Payload.coursePrice());
        courses = jsonPath.getList("courses");
    }

    @Test
    public void verifyCourseCount() {
        int count = jsonPath.getInt("courses.size()");
        Assert.assertTrue(count > 0, "Course count should be greater than 0");
        log.info(count);
    }

    @Test
    public void verifyPurchaseAmount() {
        int totalAmount = jsonPath.getInt("dashboard.purchaseAmount");
        Assert.assertTrue(totalAmount > 0, "Purchase amount should be greater than 0");
        log.info(totalAmount);
    }

    @Test
    public void verifyFirstCourseTitle() {
        String titleFirstCourse = jsonPath.get("courses[0].title");
        Assert.assertNotNull(titleFirstCourse, "First course title should not be null");
        log.info(titleFirstCourse);
    }

    @Test
    public void verifyAllCoursePricesAndTitles() {
        Assert.assertFalse(courses.isEmpty(), "Courses list should not be empty");
        courses.stream().forEach(course -> {
            Assert.assertNotNull(course.get("price"), "Course price should not be null");
            Assert.assertNotNull(course.get("title"), "Course title should not be null");
            log.info(course.get("price"));
            log.info(course.get("title"));
        });
    }

    @Test
    public void verifyRpaCopiesSold() {
        log.info("Print no of copies sold by RPA Course");
        courses.stream()
                .filter(course -> "RPA".equalsIgnoreCase((String) course.get("title")))
                .findFirst()
                .ifPresent(course -> {
                    Assert.assertNotNull(course.get("copies"), "RPA copies should not be null");
                    log.info(course.get("copies"));
                });
    }

}
