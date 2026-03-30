package com.restapi.tests.library;

import com.restapi.utils.ExcelDataReader;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

@Log4j2
public class DataDrivenSample {

    @Test
    public void dataDrivenTest() throws IOException {
        ExcelDataReader d = new ExcelDataReader();
        ArrayList data = d.getData("Add Profile", "testdata");
        log.info("{}", data.get(0));
        log.info("{}", data.get(1));
        log.info("{}", data.get(2));
        log.info("{}", data.get(3));
    }

}
