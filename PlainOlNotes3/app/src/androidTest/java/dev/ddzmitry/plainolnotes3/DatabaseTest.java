package dev.ddzmitry.plainolnotes3;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dev.ddzmitry.plainolnotes3.database.AppDatabase;
import dev.ddzmitry.plainolnotes3.database.NoteDAO;
import dev.ddzmitry.plainolnotes3.database.NoteEntity;
import dev.ddzmitry.plainolnotes3.utilities.SampleData;
import static  org.junit.Assert.*;
/**
 * Created by dzmitrydubarau on 7/18/20.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    public static final String TAG = "Junit";
    private AppDatabase mDb;
    private NoteDAO mDao;

    @Before
    public void createDb(){
        Context context = InstrumentationRegistry.getTargetContext();
        // DB ib memory
        mDb = Room.inMemoryDatabaseBuilder(context,AppDatabase.class).build();
        mDao = mDb.noteDAO();
        Log.i(TAG,"createDB");
    }

    @After
    public void closeDb(){
        mDb.close();
        Log.i(TAG, "closeDb");
    }

    @Test
    public void createAndRetrieveNotes(){
        // should send three rows
        mDao.insertAll(SampleData.getNotes());
        int count = mDao.getCount();
        Log.i(TAG,"createAndRetrieveNotes: Count= " + count);
        assertEquals(SampleData.getNotes().size(),count);
    }
    @Test
    public void compareStrings(){
        mDao.insertAll(SampleData.getNotes());
        NoteEntity origninal = SampleData.getNotes().get(0);
        NoteEntity fromDb = mDao.getNoteById(1);
        assertEquals(origninal.getText(),fromDb.getText());
        assertEquals(1,fromDb.getId());
        assertNotEquals(2,fromDb.getId());
    }
}
