package dev.ddzmitry.plainolnotes3.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import dev.ddzmitry.plainolnotes3.database.AppRepository;
import dev.ddzmitry.plainolnotes3.database.NoteEntity;
import dev.ddzmitry.plainolnotes3.utilities.SampleData;

/**
 * Created by dzmitrydubarau on 7/18/20.
 */

public class MainViewModel extends AndroidViewModel {


    public LiveData< List<NoteEntity>> mNotes;
    private AppRepository mRepository;


    public MainViewModel(@NonNull Application application) {
        super(application);

        // Get Repository Instance
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mNotes = mRepository.mNotes;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void removeAllData() {
        mRepository.removeAllData();
    }
}
