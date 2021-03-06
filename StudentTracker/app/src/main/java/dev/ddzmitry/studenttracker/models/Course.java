package dev.ddzmitry.studenttracker.models;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.ForeignKey;
import java.util.Date;

import dev.ddzmitry.studenttracker.database.CourseProgress;

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
    // PK
    private int course_id;
    // FK
    private int term_id;

    private String course_title;
    private Date course_start_date;
    private Date course_end_date;
    private String mentor;
    private String note;
    private CourseProgress courseProgress;


    public Course() {
    }

    // Constructor to use for DB
    public Course(String course_title, Date course_start_date, Date course_end_date,int term_id,CourseProgress courseProgress) {
        this.course_title = course_title;
        this.course_start_date = course_start_date;
        this.course_end_date = course_end_date;
        this.term_id = term_id;
        this.courseProgress = courseProgress;
    }

    @Ignore
    public Course(int course_id, int term_id, String course_title, Date course_start_date, Date course_end_date) {
        this.course_id = course_id;
        this.term_id = term_id;
        this.course_title = course_title;
        this.course_start_date = course_start_date;
        this.course_end_date = course_end_date;
    }

    @Ignore
    public Course(int course_id, int term_id, String course_title, Date course_start_date, Date course_end_date, String mentor, String note, CourseProgress courseProgress) {
        this.course_id = course_id;
        this.term_id = term_id;
        this.course_title = course_title;
        this.course_start_date = course_start_date;
        this.course_end_date = course_end_date;
        this.mentor = mentor;
        this.note = note;
        this.courseProgress = courseProgress;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CourseProgress getCourseProgress() {
        return courseProgress;
    }

    public void setCourseProgress(CourseProgress courseProgress) {
        this.courseProgress = courseProgress;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_id=" + course_id +
                ", term_id=" + term_id +
                ", course_title='" + course_title + '\'' +
                ", course_start_date=" + course_start_date +
                ", course_end_date=" + course_end_date +
                ", mentor=" + mentor +
                ", note='" + note + '\'' +
                ", courseProgress=" + courseProgress +
                '}';
    }
}
