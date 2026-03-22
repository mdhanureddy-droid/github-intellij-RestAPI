package files;

import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

import io.restassured.path.json.JsonPath;

@Log4j2
public class SumValidations {

    @Test
    public void sumOfCourses() {
        JsonPath jsonPath = new JsonPath(Payload.coursePrice());
        int count = jsonPath.getInt("courses.size()");
        Assert.assertTrue(count > 0, "Courses count should be greater than 0");
        int sum = IntStream.range(0, count)
                .map(i -> {
                    int amount = jsonPath.getInt("courses[" + i + "].price") * jsonPath.getInt("courses[" + i + "].copies");
                    log.info("Course {} amount: {}", i, amount);
                    return amount;
                })
                .sum();
        log.info("Total sum: {}", sum);
        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        Assert.assertEquals(sum, purchaseAmount, "Sum of courses should equal purchase amount");
    }

}
