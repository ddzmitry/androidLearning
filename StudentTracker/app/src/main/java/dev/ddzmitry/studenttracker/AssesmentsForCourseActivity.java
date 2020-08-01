package dev.ddzmitry.studenttracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.models.Assessment;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.ui.AssessmentAdapter;
import dev.ddzmitry.studenttracker.utilities.Utils;
import dev.ddzmitry.studenttracker.view.AssessmentViewModel;
import dev.ddzmitry.studenttracker.view.CourseViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_COURSE_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;

public class AssesmentsForCourseActivity extends AppCompatActivity {

    @BindView(R.id.assessmentRecyclerView)
    RecyclerView assessmentRecyclerView;

    @BindView(R.id.courseTitleText)
    TextView courseTitleText;

    @BindView(R.id.courseDateText)
    TextView courseDateText;

    // view
    private CourseViewModel courseViewModel;
    private AssessmentViewModel assessmentViewModel;
    // assessments
    private List<Assessment> assessmentsPerCourse = new ArrayList<>();
    private  Course courseToWorkWith = new Course();

    // adapter
    private AssessmentAdapter assessmentAdapter;
    private Executor executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assessments_per_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        new Thread(new Runnable() {
            public void run() {
                initRecyclerView();
                initViewModel();
            }
        }).start();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.assessments_for_course, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println(item.getItemId());

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
            intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
            startActivity(intent);
            finish();
            return true;
        } else if(item.getItemId() == R.id.menu_add_assessment){
            Intent intent = new Intent(getApplicationContext(), AssessmentActivity.class);
            intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
            startActivity(intent);
            finish();
            return true;
        }

        else if (item.toString().equals("Delete")) {
//            mViewModel.deleteNote();
            System.out.println("Hello");
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    private void initRecyclerView() {
        assessmentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AssesmentsForCourseActivity.this);
        DividerItemDecoration divider = new DividerItemDecoration(assessmentRecyclerView.getContext(), layoutManager.getOrientation());
        assessmentRecyclerView.addItemDecoration(divider);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessmentsPerCourse,AssesmentsForCourseActivity.this);
        System.out.println("initRecyclerView");
        assessmentRecyclerView.setLayoutManager(layoutManager);
        assessmentRecyclerView.setAdapter(assessmentAdapter);
    }
    private void initViewModel() {

        // establish view models
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);

//        Intent intent = getIntent();
        // Test purpose
//        intent.putExtra(KEY_ASSESSMENT_ID,1);
//        intent.putExtra(KEY_COURSE_ID,1);



            final Observer<List<Assessment>> assessmentsObserver = new Observer<List<Assessment>>() {
                @Override
                public void onChanged(@Nullable List<Assessment> assessmentsLiveData) {
                    if (assessmentsLiveData == null){

                        Toast.makeText(AssesmentsForCourseActivity.this, "No assessments yet.", Toast.LENGTH_SHORT).show();
                    } else{
                        assessmentsPerCourse.clear();
                        assessmentsPerCourse.addAll(assessmentsLiveData);
//                        System.out.println("Assessments");
//                        for (Assessment assessment : assessmentsPerCourse) {
//                            System.out.println(assessment.toString());
//                        }

                    }
                    if (assessmentAdapter == null){
                        assessmentAdapter = new AssessmentAdapter(
                                 assessmentsPerCourse,
                                AssesmentsForCourseActivity.this);

                        assessmentRecyclerView.setAdapter(assessmentAdapter);

                    } else{
                        assessmentAdapter.notifyDataSetChanged();
                    }
                }
            };


        // lets get items for display
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            System.out.println("NEW TERM");
        } else {
//            int course_id = extras.getInt(KEY_COURSE_ID);
            int course_id = 1;
            System.out.println("COURSE ID IS" + course_id);

            courseViewModel.loadCourseData(course_id);

            courseViewModel.liveCourseData.observe(this, new Observer<Course>() {
                @Override
                public void onChanged(@Nullable Course course) {
                    courseToWorkWith = course;
                    System.out.println("Course is: " + course.toString() );
                    courseTitleText.setText(course.getCourse_title());
                    courseDateText.setText(String.format("Start: %s End: %s",
                            Utils.formatDate(course.getCourse_start_date()),
                            Utils.formatDate(course.getCourse_end_date())));

                }
            });

            new Thread(new Runnable() {
                public void run() {
                    courseViewModel.loadCourseData(course_id);
                    assessmentViewModel.getAssessmentsByCourseId(course_id)
                            .observe(AssesmentsForCourseActivity.this,
                                    assessmentsObserver);
                }
            }).start();






        }

    }



}
