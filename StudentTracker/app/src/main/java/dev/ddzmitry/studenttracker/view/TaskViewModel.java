package dev.ddzmitry.studenttracker.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import dev.ddzmitry.studenttracker.database.TermsRepository;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.utilities.SampleData;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class TaskViewModel extends AndroidViewModel {

    public LiveData<List<Term>> allTerms;
    private TermsRepository termsRepository;


    public TaskViewModel(@NonNull Application application) {
        super(application);

        termsRepository = TermsRepository.getTermRepositoryInstance(application.getApplicationContext());
        System.out.println("termsRepository");
        System.out.println(termsRepository);

        allTerms = termsRepository.allTerms;

    }

    public void addSampleData(){
        termsRepository.addSampleTerms();
    }
    public void removeAllData() {
        termsRepository.RemoveAllTerms();
    }
}
