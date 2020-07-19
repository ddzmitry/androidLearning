package dev.ddzmitry.studenttracker.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import dev.ddzmitry.studenttracker.models.Term;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

@Dao
public interface TermDAO {

    // insert one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerm(Term term);

    // insert all
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTerms(List<Term> terms);

    @Delete
    void deleteTerm(Term term);

    @Query("SELECT * FROM term WHERE term_id = :id")
    Term getTermById(int id);

    @Query("SELECT * FROM term ORDER BY start_date DESC")
    LiveData<List<Term>> getAllTerms();


    @Query("DELETE FROM term")
    int deleteAllTerms();

    @Query("SELECT COUNT(*) FROM term")
    int getTermsCount();


}
