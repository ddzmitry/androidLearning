package dev.ddzmitry.studenttracker.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import dev.ddzmitry.studenttracker.models.Term;

/**
 * Created by dzmitrydubarau on 7/18/20.
 */

// can add many through comma
@Database(entities = {Term.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "C196.db";
    private static volatile AppDatabase instance;

    private static final Object LOCK = new Object();

    // For each DAO
    public abstract TermDAO termDAO();

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
