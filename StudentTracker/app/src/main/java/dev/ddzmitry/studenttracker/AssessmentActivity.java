package dev.ddzmitry.studenttracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
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
import dev.ddzmitry.studenttracker.utilities.Utils;
import dev.ddzmitry.studenttracker.view.AssessmentViewModel;
import dev.ddzmitry.studenttracker.view.CourseViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.GLOBAL_COUNTER_CHANNELS;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_ASSESSMENT_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_COURSE_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_ALARM_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_ALERT;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_DATE;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_OBJECT;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_OBJECT_ID;
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

                    editAssessmentDueDate.setText(formatDate(date));
                    editingStart = false;
                    assessmentToWorkWith.setAssessment_due_date(date);

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

                        // updates for notifications
                        String TERM_STRING_FOR_PREFS = String.format("ASSESSMENT_%s_%s",
                                assessmentToWorkWith.getAssessment_id(), "due");
                        final SharedPreferences sharedPreferences =
                                AssessmentActivity.this.getSharedPreferences("dev.ddzmitry.studenttracker",
                                        Context.MODE_PRIVATE);
                        if (sharedPreferences.getInt(TERM_STRING_FOR_PREFS, 0) != 0) {

                            createNotificationSchedule("due");

                        }


                        assessmentViewModel.saveAssessment(assessmentToWorkWith);
                        Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                        intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
                        startActivity(intent);
                        finish();

                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                        intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
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
                        intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                        intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
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

    // set Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.assessment_menu, menu);

        return true;
    }
    // Disable Menu Options
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras.getInt(KEY_ASSESSMENT_ID) == 0) {
            menu.clear();
        } else {

            Integer assessment_id = extras.getInt(KEY_ASSESSMENT_ID);
            // For notifications
            String TERM_STRING_FOR_PREFS = String.format("ASSESSMENT_%s_%s", assessment_id, "due");
            final SharedPreferences sharedPreferences =
                    AssessmentActivity.this.getSharedPreferences("dev.ddzmitry.studenttracker",
                            Context.MODE_PRIVATE);
            if (sharedPreferences.getInt(TERM_STRING_FOR_PREFS, 0) != 0) {
                // add menu item
                menu.findItem(R.id.notify_assessment).setEnabled(false);
            }

        }
        return true;
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
        }else if(item.getItemId() == R.id.notify_assessment){
            // TODO NOTIFY COURSE
            createNotificationSchedule("due");
            return true;
        } else if(item.getItemId() == R.id.delete_assessment){

            AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentActivity.this );
            builder.setTitle("Delete Assessment?");
            builder.setMessage("Would you like to delete assessment " + assessmentToWorkWith.getAssessment_name());
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    assessmentViewModel.deleteAssessment();
                    Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
                    intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
                    deleteNotificationSchedule(assessmentToWorkWith, "due");
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
        return super.onOptionsItemSelected(item);
    }

    /*
    * NOTIFICATIONS
    * */

    public void saveSharedConfigurations(String type, Assessment _assesmentToWorkWith, Integer counterId) {

        String COURSE_STRING_FOR_PREFS = String.format("ASSESSMENT_%s_%s",
                _assesmentToWorkWith.getAssessment_id(), type);
        final SharedPreferences sharedPreferences =
                this.getSharedPreferences("dev.ddzmitry.studenttracker",
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(COURSE_STRING_FOR_PREFS, counterId)
                .apply();

//        Toast.makeText(this, String.format("ASSESSMENT_STRING_FOR_PREFS %s", COURSE_STRING_FOR_PREFS)
//                + sharedPreferences.getInt(COURSE_STRING_FOR_PREFS, 0), Toast.LENGTH_SHORT).show();

        Toast.makeText(this, String.format("Setting up Notification for %s",_assesmentToWorkWith.getAssessment_name()), Toast.LENGTH_SHORT).show();

    }

    public void deleteNotificationSchedule(Assessment _assesmentToWorkWith, String type) {

        String COURSE_STRING_FOR_PREFS = String.format("ASSESSMENT_%s_%s", _assesmentToWorkWith.getAssessment_id(), type);
        final SharedPreferences sharedPreferences =
                AssessmentActivity.this.getSharedPreferences("dev.ddzmitry.studenttracker",
                        Context.MODE_PRIVATE);
        if (sharedPreferences.getInt(COURSE_STRING_FOR_PREFS, 0) != 0) {
            Integer channel_id = sharedPreferences.getInt(COURSE_STRING_FOR_PREFS, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(getApplicationContext(), MessageReciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), channel_id, myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }


    }

    public void createNotificationSchedule(String type) {
        Integer channel_counter_for_start = getGlobalChannelCounter();
        Intent intent_start = new Intent(AssessmentActivity.this, MessageReciever.class);
        intent_start.putExtra(NOTIFICATION_OBJECT, String.format("Assessment for %s : %s ",
                courseToWorkWith.getCourse_title(),
                assessmentToWorkWith.getAssessment_name()));
        intent_start.putExtra(NOTIFICATION_OBJECT_ID, assessmentToWorkWith.getAssessment_id());
        intent_start.putExtra(NOTIFICATION_DATE, Utils.formatDate(assessmentToWorkWith.getAssessment_due_date()));

        System.out.println("CREATING NOTIFICATION WITH");
        System.out.println(assessmentToWorkWith.toString());

        intent_start.putExtra(NOTIFICATION_ALERT, type);
        intent_start.putExtra(NOTIFICATION_ALARM_ID, channel_counter_for_start);
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentActivity.this, channel_counter_for_start, intent_start, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                assessmentToWorkWith.getAssessment_due_date().getTime(),
                sender);
        // save in local memory
        saveSharedConfigurations(type, assessmentToWorkWith, channel_counter_for_start);
        updateGlobalChannelCounter(channel_counter_for_start);

    }
    public Integer getGlobalChannelCounter() {
        final SharedPreferences sharedPreferences = this.getSharedPreferences("dev.ddzmitry.studenttracker", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(GLOBAL_COUNTER_CHANNELS, 0);
    }

    // for channels
    public void updateGlobalChannelCounter(Integer curr) {
        System.out.println("updateGlobalChannelCounter CURRENT IS " + curr);
        final SharedPreferences sharedPreferences = this
                .getSharedPreferences("dev.ddzmitry.studenttracker",
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(GLOBAL_COUNTER_CHANNELS, curr + 1).apply();
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

