package by.sergey.carrentapp.service.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageUtil {
    public static String getAlreadyExistsMessage(String className, String variableName, String variableValue) {

        return className + " with this " + variableName + " " + variableValue + " already exist.";
    }

    public static String getNotFoundMessage(String className, String variableName, Long variableValue) {

        return className + " with " + variableName + " " + variableValue + " does not exist.";
    }
}
