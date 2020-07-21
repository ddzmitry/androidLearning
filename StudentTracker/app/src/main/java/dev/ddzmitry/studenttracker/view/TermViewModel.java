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
        coursesRepository = CoursesRepository.getCoursesRepositoryInstance(application.getApplicationContext());
        termsRepository = TermsRepository.getTermRepositoryInstance(application.getApplicationContext());
    }

    public void loadTermData(final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Term term = termsRepository.getTermById(termId);

//                coursesRepository.addSampleCourses();
                System.out.println(coursesRepository.getCoursesByTermId(termId).getValue());
                coursesPerTerm = coursesRepository.getAllCourses();
                System.out.println("Course term is " + term.toString());


//                for (Course course : coursesPerTerm.getValue()) {
//                    System.out.println("COURSE");
//                    System.out.println(course.toString());
//                }
                liveTermData.postValue(term);
            }
        });

    }

    public void addSampleData(){
        coursesRepository.addSampleCourses();
    }
    public void removeAllData() {
        coursesRepository.removeAllCourses();
    }

}
