package org.hbrs.se2.project.util;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.Arrays;

public class Utils {

    /**
     * Nützliche Methdode zur Erweiterung eines bestehendes Arrays
     * Oma hätte gesagt, so eine Methode 'fällt nicht durch' ;-)
     *
     * https://stackoverflow.com/questions/2843366/how-to-add-new-elements-to-an-array
     */
    public static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;

    }

    // Checks if field input is null or empty
    public static boolean checkIfInputEmpty(String[] array) {
        boolean isCorrect = true;
        // checks all input to not be empty string or null
        for (String s : array) {
            if (s == null || s.equals("")) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    // exception handling
    // takes an exception as an input
    // returns the root cause message of an exception
    public static String getRootCause(Exception e) {
        Throwable rootCause = e;
        while(rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        // Split string message so we only get the last bit after key keyword
        String[] messArr = rootCause.getMessage().split("Key");
        return messArr[messArr.length - 1];
    }

    // Error Dialog
    // takes the message String as an input
    public static void makeDialog(String message) {
        Dialog dialog = new Dialog();
        dialog.add(new Text(message));
        // close button
        Button closeb = new Button("Close");
        closeb.addClickListener(event -> dialog.close());
        dialog.add(closeb);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();
    }
}

