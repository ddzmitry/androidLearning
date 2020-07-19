package dev.ddzmitry.plainolnotes3.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by dzmitrydubarau on 7/18/20.
 */

// can add many through comma
@Database(entities = {NoteEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "AppDatabase.db";
    private static volatile AppDatabase instance;

    private static final Object LOCK = new Object();

    // For each DAO
    public abstract NoteDAO noteDAO();

    // Create DB
    public static AppDatabase getInstance(Context context) {

        if(instance == null){
            synchronized (LOCK){
                if (instance == null){
                    // Create DB
                    instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME).build();

                }
            }

        }
        return instance;
    }
}
