package dev.ddzmitry.studenttracker.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

}
