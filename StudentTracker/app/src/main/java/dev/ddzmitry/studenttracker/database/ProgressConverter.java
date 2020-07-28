package dev.ddzmitry.studenttracker.database;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.util.Date;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class ProgressConverter {

    @TypeConverter
    public static String courseToString(CourseProgress courseStatus) {
        if(courseStatus == null) {
            return null;
        }
        return courseStatus.name();
    }

    @TypeConverter
    public static CourseProgress stringToCourse (String courseStatus) {
        if(TextUtils.isEmpty(courseStatus)) {
            return CourseProgress.IN_PROGRESS;
        }
        return CourseProgress.valueOf(courseStatus);
    }
}
