package dev.ddzmitry.studenttracker.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Date;
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
    public MutableLiveData<Course> liveCourseData = new MutableLiveData<>();
    public LiveData<List<Course>> allCourses;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        coursesRepository = CoursesRepository.getCoursesRepositoryInstance(this.getApplication());

    }
    public LiveData<List<Course>> getCoursesByTerm(int term_id){
        // application.getApplicationContext()
        return coursesRepository.getCoursesByTermId(term_id);
    }
    public void loadCourseData(final int courseId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Course course = coursesRepository.getCoursesById(courseId);
                liveCourseData.postValue(course);
            }
        });

    }
    public LiveData<List<Course>> getAllCourses(){
                return coursesRepository.getAllCourses();
    }

    public void saveCourse(Course updatedCourse) {
        Course course_replacement = liveCourseData.getValue();
//        course_replacement.setCourse_title(title);
//        System.out.println("SAVE_COURSE");
//        System.out.println(course_replacement.toString());

        if(course_replacement == null){
            System.out.println("NEW COURSE");
        } else {
//            note.setText(noteText.trim());
        }

        coursesRepository.insertCourse(updatedCourse);
    }

    public void addSampleData(){
        coursesRepository.addSampleCourses();
    }
    public void removeAllData() {
        coursesRepository.removeAllCourses();
    }

}
