package dev.ddzmitry.plainolnotes3.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.ddzmitry.plainolnotes3.database.AppRepository;
import dev.ddzmitry.plainolnotes3.database.NoteEntity;

/**
 * Created by dzmitrydubarau on 7/18/20.
 */

public class EditorViewModel extends AndroidViewModel {

    // Use Mutable data
    public MutableLiveData<NoteEntity> mLiveNote = new MutableLiveData<>();

    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();


    public EditorViewModel(@NonNull Application application) {

        super(application);
        mRepository = AppRepository.getInstance(this.getApplication());

    }

    public void loadData(final int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity note = mRepository.getNoteById(noteId);
                // will cause observer to trigger on change metohod
                mLiveNote.postValue(note);
            }
        });

    }

    public void saveNote(String noteText) {
        NoteEntity note = mLiveNote.getValue();

        if(note == null){
            // new note
            if(TextUtils.isEmpty(noteText.trim())){
                return;

            }
            note = new NoteEntity(new Date(),noteText.trim());


        } else {
            note.setText(noteText.trim());
        }

        mRepository.insertNote(note);
    }

    public void deleteNote() {
        mRepository.deleteNote(mLiveNote.getValue());
    }
}
