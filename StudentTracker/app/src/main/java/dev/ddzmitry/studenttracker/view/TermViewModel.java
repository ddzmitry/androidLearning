package dev.ddzmitry.studenttracker.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.ddzmitry.studenttracker.database.CoursesRepository;
import dev.ddzmitry.studenttracker.database.TermsRepository;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class TermViewModel extends AndroidViewModel {

    public LiveData<List<Course>> coursesPerTerm;
    public MutableLiveData<Term> liveTermData = new MutableLiveData<>();
    private CoursesRepository coursesRepository;
    private TermsRepository termsRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermViewModel(@NonNull Application application) {
        super(application);
        termsRepository = TermsRepository.getTermRepositoryInstance(this.getApplication());
    }

    public void loadTermData(final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Term term = termsRepository.getTermById(termId);
                liveTermData.postValue(term);
            }
        });

    }

    public void saveTerm(Term updatedTerm) {
        termsRepository.insertTerm(updatedTerm);
    }

    // courses

    public Term getTermById(int term_id ) {
        return termsRepository.getTermById(term_id);
    }
    public void  deleteTerm(){
        termsRepository.deleteTerm(liveTermData.getValue());
    }
    public MutableLiveData<Term> getLiveTermData() {
        return liveTermData;
    }

    public void addSampleData(){
        coursesRepository.addSampleCourses();
    }
    public void removeAllData() {
        coursesRepository.removeAllCourses();
    }

}
