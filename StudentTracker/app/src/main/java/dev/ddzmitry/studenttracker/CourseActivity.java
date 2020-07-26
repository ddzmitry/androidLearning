package dev.ddzmitry.studenttracker;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.view.CourseViewModel;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_COURSE_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;
import static dev.ddzmitry.studenttracker.utilities.Utils.formatDate;


public class CourseActivity extends AppCompatActivity {

    @BindView(R.id.editCourseText)
    EditText editCourseText;

    @BindView(R.id.editCourseStartDate)
    EditText editCourseStartDate;

    @BindView(R.id.editCourseEndDate)
    EditText editCourseEndDate;

    @BindView(R.id.buttonSaveUpdate)
    Button buttonSaveUpdate;

    private CourseViewModel courseViewModel;
    private Course courseToWorkWith = new Course();
    private DatePickerDialog.OnDateSetListener DatePickerDialogListener;

    Boolean isUpdating;
    Boolean editingStart, editingEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);



        initViewModel();

//        new Thread(new Runnable() {
//            public void run() {
//                initViewModel();
//            }
//        }).start();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //showDatePicker();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editCourseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editingStart = true;
                showDatePicker();


            }
        });

        DatePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // month + 1

                System.out.println(String.format("%s/%s/%s", year, month, day));
                Date date = new GregorianCalendar(year, month, day).getTime();
                if (editingStart) {
                    editCourseStartDate.setText(formatDate(date));
                    editingStart = false;
                    courseToWorkWith.setCourse_start_date(date);
                } else {
                    editCourseEndDate.setText(formatDate(date));
                    courseToWorkWith.setCourse_end_date(date);
                    editingEnd = false;
                }

            }
        };


        editCourseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editingEnd = true;
                showDatePicker();
            }
        });

        editCourseText
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.toString().isEmpty()) {
                            Toast.makeText(CourseActivity.this, "NEED TO HAVE VALUE", Toast.LENGTH_SHORT).show();
                        }else {
                            courseToWorkWith.setCourse_title(editable.toString());
                        }
                    }
                });

        buttonSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isUpdating) {
                    courseViewModel.saveCourse(courseToWorkWith);
                } else {
                    // Save Logic
                }

            }
        });


    }

    private void initViewModel() {
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);


        courseViewModel
                .liveCourseData.observe(this, new Observer<Course>() {
            @Override
            public void onChanged(@Nullable Course course) {
                if (course != null) {
                    editCourseText.setText(course.getCourse_title().toString());
                    editCourseStartDate.setText(formatDate(course.getCourse_start_date()));
                    editCourseEndDate.setText(formatDate(course.getCourse_end_date()));
                    courseToWorkWith = courseViewModel.liveCourseData.getValue();
                }
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            System.out.println("NEW COURSE");
            buttonSaveUpdate.setText("Save");
        } else {
            buttonSaveUpdate.setText("Update");
            isUpdating = true;
            int course_id = extras.getInt(KEY_COURSE_ID);
            courseViewModel.loadCourseData(course_id);

            System.out.println("WORKING WITH COURSE");
//            System.out.println(courseToWorkWith.toString());

        }

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
        return;
    }

    private void showDatePicker() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(CourseActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                DatePickerDialogListener,
                year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    private void saveAndReturn() {

        String title = editCourseText.getText().toString();
        System.out.println("TITLE IS " + title);

        // to finish activity
        finish();
    }

}
