package dev.ddzmitry.studenttracker.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.ddzmitry.studenttracker.database.AssessmentRepository;
import dev.ddzmitry.studenttracker.database.CoursesRepository;
import dev.ddzmitry.studenttracker.models.Assessment;
import dev.ddzmitry.studenttracker.models.Course;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class AssessmentViewModel extends AndroidViewModel {
    public LiveData<List<Assessment>> assessmentsPerCourse;

    private AssessmentRepository assessmentRepository;
    public MutableLiveData<Assessment> liveAssessmentData = new MutableLiveData<>();
    public LiveData<List<Assessment>> allAssessments;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        assessmentRepository = AssessmentRepository.getAssessmentRepository(this.getApplication());

    }

    public LiveData<List<Assessment>> getAssessmentsByCourseId(int course_id) {
        return assessmentRepository.getAssessmentsByCourseId(course_id);
    }

    public void loadAssessmentData(final int assessment_id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Assessment course = assessmentRepository.getAssessmentById(assessment_id);
                liveAssessmentData.postValue(course);
            }
        });

    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return assessmentRepository.getAllAssessments();

    }

    public void saveAssessment(Assessment assessment) {
//        Course course_replacement = liveCourseData.getValue();
        assessmentRepository.insertAssessment(assessment);
    }

//    public void addSampleData(){
//        coursesRepository.addSampleCourses();
//    }

    public void addOnCourseUpdates(List<Assessment> assessments) {


        assessmentRepository.addOnCourseUpdates(assessments);
    }

    public void deleteAssessment() {
        assessmentRepository.deleteAssessment(liveAssessmentData.getValue());
    }

    public void removeAllData() {
        assessmentRepository.deleteAllAssessments();
    }

}
