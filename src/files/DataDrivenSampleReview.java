package files;

import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

@Log4j2
public class DataDrivenSampleReview {

    @Test
    public void dataDrivenTest() throws IOException {
        DataDrivenReview d = new DataDrivenReview();
        ArrayList data = d.getData("Add Profile", "testdata");
        log.info("{}", data.get(0));
        log.info("{}", data.get(1));
        log.info("{}", data.get(2));
        log.info("{}", data.get(3));
    }

}
