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
    private Date assessment_due_date;
    private AssessmentType assessmentType;

    @Ignore
    public Assessment() {
    }

    public Assessment(int assessment_id, int course_id, String assessment_name, Date assessment_due_date, AssessmentType assessmentType) {
        this.assessment_id = assessment_id;
        this.course_id = course_id;
        this.assessment_name = assessment_name;
        this.assessment_due_date = assessment_due_date;
        this.assessmentType = assessmentType;
    }


    public int getAssessment_id() {
        return assessment_id;
    }
    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getAssessment_name() {
        return assessment_name;
    }

    public void setAssessment_name(String assessment_name) {
        this.assessment_name = assessment_name;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Date getAssessment_due_date() {
        return assessment_due_date;
    }

    public void setAssessment_due_date(Date assessment_due_date) {
        this.assessment_due_date = assessment_due_date;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessment_id=" + assessment_id +
                ", course_id=" + course_id +
                ", assessment_name='" + assessment_name + '\'' +
                ", assessment_due_date=" + assessment_due_date +
                ", assessmentType=" + assessmentType +
                '}';
    }
}
