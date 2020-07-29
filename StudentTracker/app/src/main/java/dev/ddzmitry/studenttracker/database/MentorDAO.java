package dev.ddzmitry.studenttracker.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Mentor;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

@Dao
public interface MentorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentor(Mentor mentor);
    // insert all
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMentors(List<Mentor> mentor);
    @Delete
    void deleteMentor(Mentor mentor);
    @Query("SELECT * FROM mentors WHERE mentor_id = :id")
    Mentor getMentorById(int id);
    @Query("SELECT * FROM mentors WHERE course_id = :id")
    LiveData<Mentor> getMentorByCourseId(int id);
    @Query("SELECT * FROM mentors ")
    LiveData<List<Mentor>> getAllMentors();
    @Query("DELETE FROM mentors")
    int deleteAllMentors();

}
