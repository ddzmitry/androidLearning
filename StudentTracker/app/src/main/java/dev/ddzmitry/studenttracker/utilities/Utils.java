package dev.ddzmitry.studenttracker.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dzmitrydubarau on 7/21/20.
 */

public class Utils {

    public static String formatDate(Date dateIN){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return  formatter.format(dateIN);
    }
}
