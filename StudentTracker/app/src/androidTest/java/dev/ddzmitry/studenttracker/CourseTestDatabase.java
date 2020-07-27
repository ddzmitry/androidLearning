package dev.ddzmitry.studenttracker;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import dev.ddzmitry.studenttracker.database.AppDatabase;
import dev.ddzmitry.studenttracker.database.CourseDAO;
import dev.ddzmitry.studenttracker.database.TermDAO;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.utilities.SampleData;
import static org.junit.Assert.assertEquals;
/**
 * Created by dzmitrydubarau on 7/20/20.
 */

public class CourseTestDatabase {

    public static final  String TAG = "Junit FOR COURSES TEST";
    private AppDatabase allDatabase;
    private CourseDAO courseDAO;
    private TermDAO termDAO;


    @Before
    public void createDb(){
        // get contect
        Context context = InstrumentationRegistry.getTargetContext();
        // build temp db
        allDatabase = Room.inMemoryDatabaseBuilder(context,AppDatabase.class).build();
        courseDAO = allDatabase.courseDAO();
        termDAO = allDatabase.termDAO();
        // Got to insert all terms
        termDAO.insertAllTerms(SampleData.getSampleTerms());
        Log.i(TAG, "Database was Created");
    }

    @After
    public void closeDb(){
        allDatabase.close();
        Log.i(TAG,"Database was Closed");
    }

    @Test
    public void  createAndRetrieveTerms(){
        courseDAO.insertAllCourses(SampleData.getSamplCourses());
        Course CourseOG = SampleData.getSamplCourses().get(0);
        Course CourseDB = courseDAO.getCourseById(1);
        assertEquals(CourseOG.getCourse_title(),CourseDB.getCourse_title());
        assertEquals(1,CourseDB.getCourse_id());
        Course TermDBId2 = courseDAO.getCourseById(2);
        assertEquals(TermDBId2.getCourse_title(),SampleData.getSamplCourses().get(1).getCourse_title());

    }

    @Test
    public void compareSizes(){
        courseDAO.insertAllCourses(SampleData.getSamplCourses());
        termDAO.deleteTerm(SampleData.getSampleTerms().get(0));
        Course courseToDelete = courseDAO.getCourseById(1);
        assertEquals(3,courseDAO.getCoursesCount());
        courseDAO.deleteCourse(courseToDelete);
        assertEquals(2,courseDAO.getCoursesCount());

    }

    @Test
    public void makeSureThatTableWillBeCleaned(){
        courseDAO.insertAllCourses(SampleData.getSamplCourses());
        assertEquals(3,courseDAO.getCoursesCount());
        courseDAO.deleteAllCourses();
        assertEquals(0,courseDAO.getCoursesCount());

    }
    @Test
    public void testForSummary(){
        //getCoursesByTermId
        courseDAO.insertAllCourses(SampleData.getSamplCourses());
        int courses = courseDAO.getCountByTerm(2);
        assertEquals(2,courses);

    }
//    @Test
//    public void updateTermCheckCourses(){
//       Term q_Term = termDAO.getTermById(1);
//        System.out.println(q_Term.toString());
//    }
//    @Test
//    public void updateTermCheckCourses(){
//        courseDAO.insertAllCourses(SampleData.getSamplCourses());
//        Term q_Term = termDAO.getTermById(1);
//        System.out.println(q_Term.toString());
//        q_Term.setTerm_title("BOO");
//        termDAO.insertTerm(q_Term);
//
//        LiveData<List<Course>> ld =  courseDAO.getCoursesByTermId(q_Term.getTerm_id());
//
//    }
}
