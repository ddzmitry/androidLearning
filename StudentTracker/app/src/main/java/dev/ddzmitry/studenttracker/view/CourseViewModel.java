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

public class CourseViewModel extends AndroidViewModel {
    public LiveData<List<Course>> coursesPerTerm;
    private CoursesRepository coursesRepository;

    public LiveData<List<Course>> allCourses;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        coursesRepository = CoursesRepository.getCoursesRepositoryInstance(this.getApplication());
        allCourses = coursesRepository.getAllCourses();
    }
    public LiveData<List<Course>> getCoursesByTerm(int term_id){
        // application.getApplicationContext()
        return coursesRepository.getCoursesByTermId(term_id);
    }

//    public void loadCourseDataFromTermID(final int termId) {
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("INSIDE loadCourseDataFromTermID TERM ID IS " + termId);
////                coursesRepository.addSampleCourses();
//                coursesPerTerm = coursesRepository.getAllCourses();
////                System.out.println("Course term is " + term.toString());
////                liveTermData.postValue(termId);
//            }
//        });
//
//    }

    public void addSampleData(){
        coursesRepository.addSampleCourses();
    }
    public void removeAllData() {
        coursesRepository.removeAllCourses();
    }

}
