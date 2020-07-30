package dev.ddzmitry.studenttracker.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.ddzmitry.studenttracker.models.Assessment;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.utilities.SampleData;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class AssessmentRepository {

    private static AssessmentRepository assessmentRepository;
    public LiveData<List<Assessment>> allAssesments;
    private AppDatabase allDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();


    public static AssessmentRepository getAssessmentRepository(Context context) {
        if (assessmentRepository == null) {
            assessmentRepository = new AssessmentRepository(context);
        }
        return assessmentRepository;
    }

    public AssessmentRepository(Context context) {
        allDatabase = AppDatabase.getInstance(context);
        allAssesments = getAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() {

        return allDatabase.assesmentDAO().getAllAssessments();
    }

    public LiveData<List<Assessment>> getAssessmentsByCourseId(int id) {

        return allDatabase.assesmentDAO().getAssessmentsByCourseId(id);
    }

    public Assessment getAssessmentById(int id) {
        return allDatabase.assesmentDAO().getAssessmentById(id);
    }

    public void deleteAssessment(final Assessment assessment) {
        executor.execute(() -> allDatabase.assesmentDAO().deleteAssessment(assessment));
    }

    public void insertAssessment(final Assessment assessment) {
        executor.execute(() -> allDatabase.assesmentDAO().insertAssessment(assessment));
    }

    public void addSampleAssessments() {
        // todo
//        System.out.println("Adding courses");
//        executor.execute(() -> allDatabase.courseDAO().insertAllCourses(SampleData.getSamplCourses()));
    }

    public void addOnCourseUpdates(List<Assessment> assessments) {
        System.out.println("Adding assesments on course updates");
        executor.execute(() -> allDatabase.assesmentDAO().insertAllAssessments(assessments));

    }

    public void deleteAllAssessments() {
        executor.execute(() -> allDatabase.assesmentDAO().deleteAllAssessments());
    }

    // Counts
    public int getCountByCourse(int id) {
        return allDatabase.assesmentDAO().getCountByCourse(id);
    }

}
