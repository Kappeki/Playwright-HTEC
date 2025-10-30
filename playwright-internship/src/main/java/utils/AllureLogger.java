package utils;

import io.qameta.allure.Step;
import io.qameta.allure.Allure;

public class AllureLogger {
    @Step("{0}")
    public static void logStep(final String message){
        Allure.step(message);
        System.out.println(message);
    }

    @Step("{0}")
    public static void beforeSuiteLog(final String message){
        System.out.println(message);
    }
}
