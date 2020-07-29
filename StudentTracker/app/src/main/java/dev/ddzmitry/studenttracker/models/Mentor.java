package dev.ddzmitry.studenttracker.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import dev.ddzmitry.studenttracker.database.CourseProgress;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */
// If Removed Cascade
@Entity(tableName = "mentors",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id",
                onDelete = ForeignKey.CASCADE))
public class Mentor {

    @PrimaryKey(autoGenerate = true)
    private int mentor_id;
    private String FullName;
    private String emailAddress;

    private int course_id;



    public Mentor
            (int mentor_id, String FullName, String emailAddress, int course_id) {
        this.mentor_id = mentor_id;
        this.FullName = FullName;
        this.emailAddress = emailAddress;
        this.course_id = course_id;
    }
    @Ignore
    public Mentor() {

    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public int getCourse_id() {
        return course_id;
    }

    public int getMentor_id() {
        return mentor_id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "Mentor{" +
                "mentor_id=" + mentor_id +
                ", FullName='" + FullName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", course_id=" + course_id +
                '}';
    }
}
