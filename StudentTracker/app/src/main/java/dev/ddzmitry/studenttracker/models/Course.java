package dev.ddzmitry.studenttracker.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */


// If Removed Cascade
@Entity(tableName = "courses",
        foreignKeys = @ForeignKey(entity = Term.class,
                parentColumns = "term_id",
                childColumns = "term_id",
                onDelete = ForeignKey.CASCADE))
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int course_id; // PK
    private int term_id; // FK
    private String course_title;
    private Date course_start_date;
    private Date course_end_date;

    public Course() {
    }

    // Constructor to use for DB
    public Course(String course_title, Date course_start_date, Date course_end_date,int term_id) {
        this.course_title = course_title;
        this.course_start_date = course_start_date;
        this.course_end_date = course_end_date;
        this.term_id = term_id;
    }

    @Ignore
    public Course(int course_id, int term_id, String course_title, Date course_start_date, Date course_end_date) {
        this.course_id = course_id;
        this.term_id = term_id;
        this.course_title = course_title;
        this.course_start_date = course_start_date;
        this.course_end_date = course_end_date;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public Date getCourse_start_date() {
        return course_start_date;
    }

    public void setCourse_start_date(Date course_start_date) {
        this.course_start_date = course_start_date;
    }

    public Date getCourse_end_date() {
        return course_end_date;
    }

    public void setCourse_end_date(Date course_end_date) {
        this.course_end_date = course_end_date;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_id=" + course_id +
                ", term_id=" + term_id +
                ", course_title='" + course_title + '\'' +
                ", course_start_date=" + course_start_date +
                ", course_end_date=" + course_end_date +
                '}';
    }
}
