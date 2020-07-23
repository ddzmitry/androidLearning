package dev.ddzmitry.studenttracker;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.view.CourseViewModel;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_COURSE_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;
import static dev.ddzmitry.studenttracker.utilities.Utils.formatDate;

public class CourseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.editCourseText)
    EditText editCourseText;

    private CourseViewModel courseViewModel;
    private Course courseToWorkWith;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        initViewModel();

        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initViewModel() {
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);


        courseViewModel.liveCourseData.observe(this, new Observer<Course>() {
            @Override
            public void onChanged(@Nullable Course course) {
                if(course != null){
//                    System.out.println(course.toString());
//                    System.out.println(course.getCourse_title());
                    editCourseText.setText(course.getCourse_title().toString());
                    courseViewModel.saveCourse();

                }
            }
        });


        Bundle  extras = getIntent().getExtras();
        if(extras == null){
            System.out.println("NEW COURSE");
        } else {
            int course_id = extras.getInt(KEY_COURSE_ID);
            courseViewModel.loadCourseData(course_id);
        }

    }

    private void showDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // date that was selected
        // month +1
        String date = String.format("%s/%s/%s",year,month,day);
        System.out.println(date);
    }

    private void saveAndReturn() {
        courseViewModel.saveCourse();
        // to finish activity
        finish();
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();

    }
}
