package dev.ddzmitry.studenttracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.utilities.SampleData;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class TermsRepository {


    private static TermsRepository TermRepositoryInstance;
    private TermDAO termDAO;
    private LiveData<List<Term>> allTerms;
    private AppDatabase allDatabase;
    // For Processes on separated threat
    private Executor executor = Executors.newSingleThreadExecutor();


    public static TermsRepository getTermRepositoryInstance(Context context) {
        if (TermRepositoryInstance == null) {
            TermRepositoryInstance = new TermsRepository(context);
        }
        return TermRepositoryInstance;
    }

    private TermsRepository(Context context) {
        // Get instance of database
        allDatabase = AppDatabase.getInstance(context);
        // Gett all terms
        allTerms = getAllTerms();
    }


    public LiveData<List<Term>> getAllTerms() {
        // go to DAO and get all terms
        return termDAO.getAllTerms();
    }

    public void RemoveAllTerms() {
        executor.execute(() -> allDatabase.termDAO().deleteAllTerms());
    }

    public void addSampleTerms() {

        executor.execute(() -> allDatabase.termDAO().insertAllTerms(SampleData.getSampleTerms()));
    }

    public Term getTermById(final int term_id) {
        return allDatabase.termDAO().getTermById(term_id);
    }

    public void insertTerm(final Term term) {
        executor.execute(() -> allDatabase.termDAO().insertTerm(term));
    }

    public void deleteTerm(final Term term) {
        executor.execute(() -> allDatabase.termDAO().deleteTerm(term));
    }

}
