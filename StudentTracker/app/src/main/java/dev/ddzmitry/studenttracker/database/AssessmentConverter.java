package dev.ddzmitry.studenttracker.database;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class AssessmentConverter {

    @TypeConverter
    public static String typeToString(AssessmentType assessmentType) {
        if(assessmentType == null) {
            return null;
        }
        return assessmentType.name();
    }

    @TypeConverter
    public static AssessmentType stringToType (String assessmentType) {

        return AssessmentType.valueOf(assessmentType);
    }
}
