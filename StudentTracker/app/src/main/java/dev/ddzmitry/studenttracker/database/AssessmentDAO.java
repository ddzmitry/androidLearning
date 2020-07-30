package dev.ddzmitry.studenttracker.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import dev.ddzmitry.studenttracker.models.Assessment;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

@Dao
public interface AssessmentDAO {

    // insert one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(Assessment assessment);

    // insert all
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAssessments(List<Assessment> assessments);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("SELECT * FROM assessments WHERE assessment_id = :id")
    Assessment getAssessmentById(int id);

    @Query("SELECT * FROM assessments WHERE course_id = :id")
    LiveData<List<Assessment>> getAssessmentsByCourseId(int id);


    @Query("SELECT * FROM assessments ")
    LiveData<List<Assessment>> getAllAssessments();


    @Query("DELETE FROM assessments")
    int deleteAllAssessments();

    @Query("SELECT COUNT(*) FROM assessments")
    int getCoursesAssessments();

    // For Summary
    @Query("SELECT COUNT(*) FROM assessments WHERE course_id = :id")
    int getCountByCourse(int id);


}
