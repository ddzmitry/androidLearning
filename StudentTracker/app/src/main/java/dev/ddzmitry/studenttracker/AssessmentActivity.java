package dev.ddzmitry.studenttracker;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.database.AssessmentType;
import dev.ddzmitry.studenttracker.models.Assessment;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.view.AssessmentViewModel;
import dev.ddzmitry.studenttracker.view.CourseViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_ASSESSMENT_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_COURSE_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;
import static dev.ddzmitry.studenttracker.utilities.Utils.formatDate;

public class AssessmentActivity extends AppCompatActivity {

    @BindView(R.id.editAssessmentText)
    EditText editAssessmentText;

    @BindView(R.id.editAssessmentDueDate)
    EditText editAssessmentDueDate;

    @BindView(R.id.buttonSaveUpdate)
    Button buttonSaveUpdate;

    @BindView(R.id.editAssessmentSpinner)
    Spinner editAssessmentSpinner;

    @BindView(R.id.fab_delete_assesment)
    FloatingActionButton fab_delete_assesment;


    // view models
    private CourseViewModel courseViewModel;
    private AssessmentViewModel assessmentViewModel;
    private DatePickerDialog.OnDateSetListener DatePickerDialogListener;
    // for spinner
    private ArrayAdapter<AssessmentType> assessmentTypeArrayAdapter;
    // for work
    private Course courseToWorkWith = new Course();
    private Assessment assessmentToWorkWith = new Assessment();
    boolean editingStart = false;
    boolean editingEnd = false;
    boolean isUpdating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initViewModel();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_delete_assesment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentActivity.this );
                builder.setTitle("Delete Assessment?");
                builder.setMessage("Would you like to delete assessment " + assessmentToWorkWith.getAssessment_name());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        assessmentViewModel.deleteAssessment();
                        Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                        intent.putExtra(KEY_ASSESSMENT_ID, courseToWorkWith.getCourse_id());
                        startActivity(intent);
                        finish();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        editAssessmentDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editingEnd = true;
                showDatePicker();
            }
        });

        DatePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date date = new GregorianCalendar(year, month, day).getTime();
                if (editingStart) {
                    editAssessmentDueDate.setText(formatDate(date));
                    editingStart = false;
                    assessmentToWorkWith.setAssessment_due_date(date);
                }
            }
        };

        editAssessmentText
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
                            Toast.makeText(AssessmentActivity.this, "NEED TO HAVE VALUE", Toast.LENGTH_SHORT).show();
                        } else {
                            assessmentToWorkWith.setAssessment_name(editable.toString());
                        }
                    }
                });

        buttonSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndReturn();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),
                AssesmentsForCourseActivity.class);
        intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(),
                    AssesmentsForCourseActivity.class);
            intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public String ValidateAssessment(Assessment assessmentToWorkWith) {
        if (assessmentToWorkWith.getAssessment_name() == null) {
            return "Need Title for assessment.";
        }
        return null;

    }
    private void saveAndReturn(){
        String validator = ValidateAssessment(assessmentToWorkWith);
        if (validator == null) {

            if(isUpdating){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Update Assessment?");
                builder.setMessage("Would you like to update assessment " + assessmentToWorkWith.getAssessment_name());
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        assessmentToWorkWith.setAssessmentType((AssessmentType) editAssessmentSpinner.getSelectedItem());
                        assessmentViewModel.saveAssessment(assessmentToWorkWith);
                        Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                        intent.putExtra(KEY_ASSESSMENT_ID, courseToWorkWith.getCourse_id());
                        startActivity(intent);
                        finish();

                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                        intent.putExtra(KEY_ASSESSMENT_ID, courseToWorkWith.getCourse_id());
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();


            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Assessment?");
                builder.setMessage("Would you like to save assessment " + assessmentToWorkWith.getAssessment_name());
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        assessmentToWorkWith.setAssessmentType((AssessmentType) editAssessmentSpinner.getSelectedItem());
                        System.out.println("Saving Assesment " + assessmentToWorkWith.toString());

                        assessmentViewModel.saveAssessment(assessmentToWorkWith);
                        Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                        intent.putExtra(KEY_ASSESSMENT_ID, courseToWorkWith.getCourse_id());
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                        intent.putExtra(KEY_ASSESSMENT_ID, courseToWorkWith.getCourse_id());
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            }



        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Input Error!");
            builder.setMessage(validator);
            builder.show();
        }

    }



    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Set current Date to default
        DatePickerDialog dialog = new DatePickerDialog(AssessmentActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                DatePickerDialogListener,
                year, month, day);
        // That will allow only pick dates in range of the current course
        dialog.getDatePicker().setMinDate(courseToWorkWith.getCourse_start_date().getTime());
        dialog.getDatePicker().setMaxDate(courseToWorkWith.getCourse_end_date().getTime());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void initSpinner() {
        assessmentTypeArrayAdapter = new ArrayAdapter<AssessmentType>(this,android.R.layout.simple_list_item_1,AssessmentType.values());
        editAssessmentSpinner.setAdapter(assessmentTypeArrayAdapter);

    }




    private void initViewModel() {
        initSpinner();
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        Bundle extras = getIntent().getExtras();

        Intent intent = getIntent();
        // Test purpose
//        intent.putExtra(KEY_ASSESSMENT_ID,1);
//        intent.putExtra(KEY_COURSE_ID,1);


        if (intent.hasExtra(KEY_COURSE_ID)) {
            Integer course_id = extras.getInt(KEY_COURSE_ID);
//            Integer course_id = 1 ;
            buttonSaveUpdate.setText("Create");
            courseViewModel.loadCourseData(course_id);
            courseViewModel.liveCourseData.observe(this, new Observer<Course>() {
                @Override
                public void onChanged(@Nullable Course course) {
                    // if course updates
                    courseToWorkWith = course;
                    // set due date automatically for ease
                    assessmentToWorkWith.setCourse_id(courseToWorkWith.getCourse_id());
                    assessmentToWorkWith.setAssessment_due_date(courseToWorkWith.getCourse_end_date());
                    System.out.println(assessmentToWorkWith.toString());
                    editAssessmentDueDate.setText(formatDate(courseToWorkWith.getCourse_end_date()));
                    //

//                    System.out.println(courseToWorkWith.toString());
                }
            });


            System.out.println("NEW ASSESMENT");

        }
        else if (intent.hasExtra(KEY_ASSESSMENT_ID)){
             Integer assesment_id = extras.getInt(KEY_ASSESSMENT_ID);
//            Integer assesment_id = 6 ;
            isUpdating = true;
            // so we can delete it
            buttonSaveUpdate.setText("Update");
            fab_delete_assesment.setVisibility(View.VISIBLE);
            assessmentViewModel.loadAssessmentData(assesment_id);

            assessmentViewModel
                    .liveAssessmentData
                    .observe(this, new Observer<Assessment>() {
                @Override
                public void onChanged(@Nullable Assessment assessment) {
                    System.out.println(assessment.toString());
                    assessmentToWorkWith = assessment;
                    editAssessmentText.setText(assessment.getAssessment_name());
                    editAssessmentDueDate.setText(formatDate(assessment.getAssessment_due_date()));
                    int position = assessmentTypeArrayAdapter.getPosition(assessmentToWorkWith.getAssessmentType());
                    editAssessmentSpinner.setSelection(position);
                    editAssessmentSpinner.setEnabled(true);
                    courseViewModel.loadCourseData(assessment.getCourse_id());
                    courseViewModel.liveCourseData.observe(AssessmentActivity.this, new Observer<Course>() {
                        @Override
                        public void onChanged(@Nullable Course course) {
                            courseToWorkWith = course;
                            System.out.println(courseToWorkWith.toString());
                        }
                    });


                }
            });



            System.out.println("UPDATING ASSESMENT");
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

    }

}

