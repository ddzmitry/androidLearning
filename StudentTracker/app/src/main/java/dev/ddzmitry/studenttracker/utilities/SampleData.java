package dev.ddzmitry.studenttracker.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class SampleData {

    private static Date getDate(int diff){
        // current date
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND,diff);
        return cal.getTime();
    }


    public static List<Term> getSampleTerms(){
        List<Term> terms = new ArrayList<>();
        terms.add(new Term("Term1",getDate(10000),getDate(1000000)));
        terms.add(new Term("Term2",getDate(50000),getDate(6000000)));
        terms.add(new Term("Term3",getDate(800),getDate(10000000)));
        return terms;
    }

    public static List<Course> getSamplCourses(){

        List<Course> courses = new ArrayList<>();
        courses.add(new Course("A123",getDate(10000),getDate(1000000),1));
        courses.add(new Course("B123",getDate(50000),getDate(6000000),2));
        courses.add(new Course("C123",getDate(800),getDate(10000000),3));
        courses.add(new Course("D123",getDate(10000),getDate(1000000),1));
        courses.add(new Course("E123",getDate(50000),getDate(6000000),2));
        courses.add(new Course("F123",getDate(800),getDate(10000000),3));
        courses.add(new Course("G123",getDate(10000),getDate(1000000),1));
        courses.add(new Course("H123",getDate(50000),getDate(6000000),2));
        courses.add(new Course("I123",getDate(800),getDate(10000000),3));
        return courses;
    }

}
