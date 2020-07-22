package dev.ddzmitry.studenttracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.utilities.SampleData;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class CoursesRepository {

    private static CoursesRepository coursesRepositoryInstance;
    public LiveData<List<Course>> allCourses;
    private AppDatabase allDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();


    public static CoursesRepository getCoursesRepositoryInstance(Context context) {
        if (coursesRepositoryInstance == null) {
            coursesRepositoryInstance = new CoursesRepository(context);
        }
        return coursesRepositoryInstance;
    }

    public CoursesRepository(Context context) {
        allDatabase = AppDatabase.getInstance(context);
        allCourses = getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allDatabase.courseDAO().getAllCourses();
    }

    public LiveData<List<Course>> getCoursesByTermId(int id) {

        return allDatabase.courseDAO().getCoursesByTermId(id);
    }

    public Course getCoursesById(int id) {
        return allDatabase.courseDAO().getCourseById(id);
    }

    public void deleteCourse(final Course course) {
        executor.execute(() -> allDatabase.courseDAO().deleteCourse(course));
    }

    public void insertCourse(final Course course) {
        executor.execute(() -> allDatabase.courseDAO().insertCourse(course));
    }

    public void addSampleCourses() {
        System.out.println("Adding courses");
        executor.execute(() -> allDatabase.courseDAO().insertAllCourses(SampleData.getSamplCourses()));
    }

    public void removeAllCourses() {
        executor.execute(() -> allDatabase.courseDAO().deleteAllCourses());
    }

    // Counts
    public int getCountByTermId(int id) {
        return allDatabase.courseDAO().getCountByTerm(id);
    }

}
