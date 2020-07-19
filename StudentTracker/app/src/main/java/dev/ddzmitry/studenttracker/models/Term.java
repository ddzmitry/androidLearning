package dev.ddzmitry.studenttracker.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

// Create model for term
@Entity(tableName = "term")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int term_id;
    private String term_title;
    private Date start_date;
    private Date end_date;

    // Set constructors
    @Ignore
    public Term() {
    }
    @Ignore
    public Term(int term_id, String term_title, Date start_date, Date end_date) {
        this.term_id = term_id;
        this.term_title = term_title;
        this.start_date = start_date;
        this.end_date = end_date;
    }
    // Constructor for DB to use since ID is autoincremented
    public Term(String term_title, Date start_date, Date end_date) {
        this.term_title = term_title;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public String getTerm_title() {
        return term_title;
    }

    public void setTerm_title(String term_title) {
        this.term_title = term_title;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "Term{" +
                "term_id=" + term_id +
                ", term_title='" + term_title + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                '}';
    }
}
