package org.hbrs.se2.project.util;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import org.hbrs.se2.project.dtos.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

public class Utils {

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
     * Checks if a field in an array is empty
     *
     * @param array Takes a String array
     * @return True, if an empty field is found, otherwise false
     */
    public static boolean checkIfInputEmpty(String[] array) {
        boolean empty = false;
        // checks all input to not be empty string or null
        for (String s : array) {
            if (s == null || s.equals("")) {
                empty = true;
                break;
            }
        }
        return empty;
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
     * Creates an Error Dialog
     *
     * @param message Takes the message String as an input
     */
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

    /**
     * Returns the current logged in User from Vaadin session
     *
     * @return The currently logged in user as UserDTO
     */
    public static UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
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

