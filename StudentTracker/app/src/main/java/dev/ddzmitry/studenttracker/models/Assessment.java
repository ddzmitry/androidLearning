package dev.ddzmitry.studenttracker.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import dev.ddzmitry.studenttracker.database.AssessmentType;
import dev.ddzmitry.studenttracker.database.CourseProgress;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */
// If Removed Cascade
@Entity(tableName = "assessments",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id",
                onDelete = ForeignKey.CASCADE))

public class Assessment {

    @PrimaryKey(autoGenerate = true)
    // PK
    private int assessment_id;
    // FK
    private int course_id;
    private String assessment_name;
    private Date assessment_start_date;
    private Date assessment_end_date;
    private AssessmentType assessmentType;

    @Ignore
    public Assessment() {
    }

    public Assessment(int assessment_id, int course_id, String assessment_name, Date assessment_start_date, Date assessment_end_date, AssessmentType assessmentType) {
        this.assessment_id = assessment_id;
        this.course_id = course_id;
        this.assessment_name = assessment_name;
        this.assessment_start_date = assessment_start_date;
        this.assessment_end_date = assessment_end_date;
        this.assessmentType = assessmentType;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessment_id=" + assessment_id +
                ", course_id=" + course_id +
                ", assessment_name='" + assessment_name + '\'' +
                ", assessment_start_date=" + assessment_start_date +
                ", assessment_end_date=" + assessment_end_date +
                ", assessmentType=" + assessmentType +
                '}';
    }
}
