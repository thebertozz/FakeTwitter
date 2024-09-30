package utils;

import java.text.DateFormat;
import java.util.Date;

public class Utils {

    public static boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String formatDate(long dateInMillis) {
        Date date = new Date(dateInMillis);
        return DateFormat.getDateTimeInstance().format(date);
    }
}
