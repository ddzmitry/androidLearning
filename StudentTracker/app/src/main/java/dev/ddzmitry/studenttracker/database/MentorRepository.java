package dev.ddzmitry.studenttracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Mentor;
import dev.ddzmitry.studenttracker.utilities.SampleData;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class MentorRepository {

    private static MentorRepository mentorRepository;
    public LiveData<List<Mentor>> allMentors;
    private AppDatabase allDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();


    public static MentorRepository getCoursesRepositoryInstance(Context context) {
        if (mentorRepository == null) {
            mentorRepository = new MentorRepository(context);
        }
        return mentorRepository;
    }

    public MentorRepository(Context context) {
        allDatabase = AppDatabase.getInstance(context);
    }
    public void insertMentor(final Mentor mentor) {
        executor.execute(() -> allDatabase.mentorDAO().insertMentor(mentor));
    }
    public void deleteMentor(final Mentor mentor) {
        executor.execute(() -> allDatabase.mentorDAO().deleteMentor(mentor));
    }
    public LiveData<List<Mentor>> getAllMentors() {
        return allDatabase.mentorDAO().getAllMentors();
    }
    public LiveData<Mentor> getMentorByCourseId(int id) {
        return allDatabase.mentorDAO().getMentorByCourseId(id);
    }

    public Mentor getMentorById(int id) {
        return
                allDatabase.mentorDAO().getMentorById(id);
    }

    public void deleteAllMentors() {
        executor.execute(() -> allDatabase.mentorDAO().deleteAllMentors());
    }


    public void insertAllMentors() {
        // TODO implement
//        executor.execute(() -> allDatabase.mentorDAO()
//                .insertAllMentors();
    }
}
