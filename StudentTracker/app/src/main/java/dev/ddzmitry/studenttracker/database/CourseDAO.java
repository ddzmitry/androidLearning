package dev.ddzmitry.studenttracker.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

@Dao
public interface CourseDAO {

//    // insert one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(Course course);
//
    // insert all
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCourses(List<Course> courses);

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM courses WHERE course_id = :id")
    Course getCourseById(int id);

    @Query("SELECT * FROM courses WHERE term_id = :id")
    LiveData<List<Course>> getCoursesByTermId(int id);


    @Query("SELECT * FROM courses ")
    LiveData<List<Course>> getAllCourses();


    @Query("DELETE FROM courses")
    int deleteAllCourses();

    @Query("SELECT COUNT(*) FROM courses")
    int getCoursesCount();

    // For Summary
    @Query("SELECT COUNT(*) FROM courses WHERE term_id = :id")
    int getCountByTerm(int id);


}
