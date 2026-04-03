package com.restapi.listeners;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Optional;

@Log4j2
public class ApiRetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 2;
    private static final int EXPECTED_STATUS_CODE = 200;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT && isNon200StatusCodeFailure(result)) {
            retryCount++;
            log.warn("Retrying test '{}' due to non-200 status code. Attempt {}/{}",
                    result.getMethod().getMethodName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }

    private boolean isNon200StatusCodeFailure(ITestResult result) {
        return Optional.ofNullable(result.getThrowable())
                .map(Throwable::getMessage)
                .filter(msg -> StringUtils.contains(msg, "status code"))
                .filter(msg -> !StringUtils.contains(msg, String.valueOf(EXPECTED_STATUS_CODE)))
                .isPresent();
    }
}
