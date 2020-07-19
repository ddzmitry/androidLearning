package dev.ddzmitry.plainolnotes3.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.ddzmitry.plainolnotes3.utilities.SampleData;

/**
 * Created by dzmitrydubarau on 7/18/20.
 */

public class AppRepository {
    private static  AppRepository ourInstance;

    public LiveData<List<NoteEntity>> mNotes;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();


    public static AppRepository getInstance(Context context) {
        if (ourInstance == null){
            ourInstance  = new AppRepository(context);

        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        // set DB first
        mDb = AppDatabase.getInstance(context);
        mNotes =  getAllNotes();

    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
//                System.out.println("SAMPLE DATA");
//                System.out.println(SampleData.getNotes());
                mDb.noteDAO().insertAll(SampleData.getNotes());
            }
        });

    }

    private  LiveData<List<NoteEntity>> getAllNotes(){
        return mDb.noteDAO().getAll();
    }

    public void removeAllData() {
        // if return is
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDAO().deleteAll();

            }
        });
    }

    public NoteEntity getNoteById(final int noteId) {

        return  mDb.noteDAO().getNoteById(noteId);

    }

    public void insertNote(final NoteEntity note) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDAO().insertNote(note);
            }
        });
    }

    public void deleteNote(final NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDAO().deleteNote(note);
            }
        });
    }
}
