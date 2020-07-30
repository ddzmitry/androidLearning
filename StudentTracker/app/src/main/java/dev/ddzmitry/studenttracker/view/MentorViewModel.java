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
import dev.ddzmitry.studenttracker.database.MentorRepository;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Mentor;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class MentorViewModel extends AndroidViewModel {
    private MentorRepository mentorRepository;
    public MutableLiveData<Mentor> liveMentorData = new MutableLiveData<>();
    private Executor executor = Executors.newSingleThreadExecutor();

    public MentorViewModel(@NonNull Application application) {
        super(application);
        mentorRepository = MentorRepository
                .getCoursesRepositoryInstance
                        (this.getApplication());

    }
    public LiveData<Mentor> getMentorByCourseId(int course_id){
        return mentorRepository.getMentorByCourseId(course_id);
    }

    public void insertMentor (Mentor mentor) {

        mentorRepository.insertMentor(mentor);
    }

}
