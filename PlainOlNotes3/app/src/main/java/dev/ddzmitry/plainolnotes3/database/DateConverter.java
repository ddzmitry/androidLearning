package dev.ddzmitry.plainolnotes3.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by dzmitrydubarau on 7/18/20.
 */

public class DateConverter {

    // date to int

    @TypeConverter
    public static Date toDate(Long timestamp){
        return timestamp == null ? null : new Date(timestamp);
    }

    // int to date

    @TypeConverter
    public static Long toTimestamp(Date date){
        return date == null ? null : date.getTime();
    }
}
