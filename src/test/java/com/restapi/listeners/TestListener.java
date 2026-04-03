package com.restapi.listeners;

import lombok.extern.log4j.Log4j2;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

@Log4j2
public class TestListener implements ITestListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        Class<? extends IRetryAnalyzer> existingRetry = annotation.getRetryAnalyzerClass();
        if (existingRetry == null) {
            annotation.setRetryAnalyzer(ApiRetryAnalyzer.class);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Starting test: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = Optional.ofNullable(result.getThrowable())
                .map(Throwable::getMessage)
                .orElse("Unknown error");
        log.error("Test FAILED: {} - {}", testName, errorMessage);

        IRetryAnalyzer retryAnalyzer = result.getMethod().getRetryAnalyzer(result);
        if (retryAnalyzer != null) {
            log.info("Retry analyzer '{}' is attached to test '{}'",
                    retryAnalyzer.getClass().getSimpleName(), testName);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test SKIPPED: {}", result.getMethod().getMethodName());
    }
}
