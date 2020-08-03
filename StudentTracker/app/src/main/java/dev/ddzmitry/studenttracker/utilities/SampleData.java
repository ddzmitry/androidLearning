package dev.ddzmitry.studenttracker.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import dev.ddzmitry.studenttracker.database.CourseProgress;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class SampleData {

    private static Date getDate(int diff){
        // current date
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH,diff);
        return cal.getTime();
    }


    public static List<Term> getSampleTerms(){
        List<Term> terms = new ArrayList<>();
        terms.add(new Term("Term1",getDate(0),getDate(6)));
        terms.add(new Term("Term2",getDate(1),getDate(7)));
        return terms;
    }

    public static List<Course> getSamplCourses(){
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("C195-Software II - Advanced Java Concepts",getDate(1),getDate(2),1, CourseProgress.PLANNED));
        courses.add(new Course("C857-Software Quality Assurance",getDate(2),getDate(3),1, CourseProgress.IN_PROGRESS));
        courses.add(new Course("C111-Software Dropped Course",getDate(1),getDate(2),1, CourseProgress.DROPPED));
        courses.add(new Course("C196-Mobile Application Development",getDate(4),getDate(5),1, CourseProgress.COMPLETED));
        return courses;
    }

}
