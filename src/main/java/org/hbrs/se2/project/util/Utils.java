package org.hbrs.se2.project.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class; cannot be instantiated!");
    }

    /**
     * Adding a generic element to an array of the same type
     *
     * @param arr A generic array
     * @param element An generic element
     * @return The modified generic array
     * @see <a href="https://stackoverflow.com/questions/2843366/how-to-add-new-elements-to-an-array">https://stackoverflow.com/questions/2843366/how-to-add-new-elements-to-an-array</a>
     */
    public static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;

    }

    /**
     * Exception handling
     *
     * @param e Takes an exception as an input
     * @return The root cause message of an exception
     */
    public static String getRootCause(Exception e) {
        Throwable rootCause = e;
        while(rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        // Split string message, so we only get the last bit after key keyword
        String[] messArr = rootCause.getMessage().split("Key");
        return messArr[messArr.length - 1];
    }

    /**
     * Creates a logger with specified name
     *
     * @param s the logger identifier, for example class name
     * @return Returns a newly created logger
     */
    public static Logger getLogger(String s) {
        return LoggerFactory.getLogger(s);
    }

}
