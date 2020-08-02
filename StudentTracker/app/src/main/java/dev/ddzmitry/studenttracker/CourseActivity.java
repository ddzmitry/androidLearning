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
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.database.CourseProgress;
import dev.ddzmitry.studenttracker.models.Assessment;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Mentor;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.utilities.Utils;
import dev.ddzmitry.studenttracker.view.AssessmentViewModel;
import dev.ddzmitry.studenttracker.view.CourseViewModel;
import dev.ddzmitry.studenttracker.view.MentorViewModel;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.GLOBAL_COUNTER_CHANNELS;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_COURSE_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_ALARM_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_ALERT;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_DATE;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_OBJECT;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_OBJECT_ID;
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

    @BindView(R.id.editCourseSpinner)
    Spinner editCourseSpinner;
    // Notification
    @BindView(R.id.NotePhoneReciever)
    EditText NotePhoneReciever;
    @BindView(R.id.NotifyBtn)
    Button NotifyBtn;

    // mentor props
    @BindView(R.id.editCourseMentorEmail)
    EditText editCourseMentorEmail;
    @BindView(R.id.editCourseMentorName)
    EditText editCourseMentorName;
    @BindView(R.id.editCourseMentorPhone)
    EditText editCourseMentorPhone;


    @BindView(R.id.editCourseNote)
    EditText editCourseNote;

    // View Models
    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;
    private AssessmentViewModel assessmentViewModel;

    // Classes
    private Mentor mentorToWorkWith = new Mentor();
    private Course courseToWorkWith = new Course();
    private Term parentTerm = new Term();
    private DatePickerDialog.OnDateSetListener DatePickerDialogListener;

    Boolean isUpdating = false;
    Boolean editingStart = false;
    Boolean editingEnd = false;
    // for spinner
    private ArrayAdapter<CourseProgress> courseProgressArrayAdapter;
    private List<Assessment> assessmentsPerCourse = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        initViewModel();


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
                        } else {
                            courseToWorkWith.setCourse_title(editable.toString());
                        }
                    }
                });
        editCourseMentorEmail.addTextChangedListener(new TextWatcher() {
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
                } else {
                    mentorToWorkWith.setEmailAddress(editable.toString());
                }

            }
        });

        editCourseMentorName.addTextChangedListener(new TextWatcher() {
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
                } else {
                    mentorToWorkWith.setFullName(editable.toString());
                }

            }
        });

        editCourseMentorPhone.addTextChangedListener(new TextWatcher() {
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
                } else {
                    mentorToWorkWith.setPhone(editable.toString());
                }
            }
        });


        buttonSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isUpdating) {
                    saveAndReturn();
                } else {
                    saveAndReturn();
                }

            }
        });

        // Notification NotifyBtn
        NotifyBtn.setOnClickListener(view -> {
            Pattern p = Pattern.compile("(?:\\(\\d{3}\\)|\\d{3}[-]*)\\d{3}[-]*\\d{4}");
            if (p.matcher(NotePhoneReciever.getText().toString()).matches()) {
                System.out.println("VALID PHONE NUMBER");
                smsSendMessage(NotePhoneReciever.getText().toString());
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                builder.setTitle("Input Error!");
                builder.setMessage("INVALID PHONE NUMBER");
                builder.show();
            }

        });

    }

    // Disable Menu Options
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras.getInt(KEY_COURSE_ID) == 0) {
            menu.clear();
        } else {

            Integer course_id = extras.getInt(KEY_COURSE_ID);
            // For notifications
            String TERM_STRING_FOR_PREFS = String.format("COURSE_%s_%s", course_id, "start");
            final SharedPreferences sharedPreferences =
                    CourseActivity.this.getSharedPreferences("dev.ddzmitry.studenttracker",
                            Context.MODE_PRIVATE);
            if (sharedPreferences.getInt(TERM_STRING_FOR_PREFS, 0) != 0) {
                // add menu item
                menu.findItem(R.id.notify_course).setEnabled(false);
            }

        }
        return true;
    }


    private void initSpinner() {
        courseProgressArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CourseProgress.values());
        editCourseSpinner.setAdapter(courseProgressArrayAdapter);
    }


    private void initViewModel() {
        initSpinner();
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);

        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();

        if (intent.hasExtra(KEY_COURSE_ID)) {


            isUpdating = true;
            Integer course_id = extras.getInt(KEY_COURSE_ID);
            buttonSaveUpdate.setText("Update");
            courseViewModel.loadCourseData(course_id);


            courseViewModel
                    .liveCourseData.observe(this, new Observer<Course>() {
                @Override
                public void onChanged(@Nullable Course course) {
                    if (course != null) {

                        // on separated thread
                        new Thread(new Runnable() {
                            public void run() {
                                parentTerm = termViewModel.getTermById(course.getTerm_id());
                            }
                        }).start();
                        System.out.println("initViewModel");

                        assessmentViewModel.getAssessmentsByCourseId(course.getCourse_id()).observe(CourseActivity.this, new Observer<List<Assessment>>() {
                            @Override
                            public void onChanged(@Nullable List<Assessment> assessments) {
                                // this is if there is an update
                                assessmentsPerCourse.addAll(assessments);
                                for (Assessment assessment : assessmentsPerCourse) {
                                    System.out.println("IN COURSE ACTIVITY");
                                    System.out.println(assessment.toString());
                                }
                            }
                        });


                        editCourseText.setText(course.getCourse_title().toString());
                        editCourseStartDate.setText(formatDate(course.getCourse_start_date()));
                        editCourseEndDate.setText(formatDate(course.getCourse_end_date()));
                        editCourseNote.setText(course.getNote());
                        // Using ArrayList
                        if (course.getMentor() != null) {
                            List<String> arrParams = new ArrayList<String>();
                            String str[] = course.getMentor().split("\\|");
                            arrParams = Arrays.asList(str);
                            mentorToWorkWith.setFullName(arrParams.get(0));
                            mentorToWorkWith.setEmailAddress(arrParams.get(1));
                            mentorToWorkWith.setPhone(arrParams.get(2));
                            editCourseMentorEmail.setText(mentorToWorkWith.getEmailAddress());
                            editCourseMentorName.setText(mentorToWorkWith.getFullName());
                            editCourseMentorPhone.setText(mentorToWorkWith.getPhone());

                        }

                        editCourseNote.setText(course.getNote());
                        courseToWorkWith = courseViewModel.liveCourseData.getValue();
//                        System.out.println("COURSE_TO_WORK");
//                        System.out.println(courseToWorkWith.toString());
                        // set spinner
                        int position = courseProgressArrayAdapter.getPosition(courseToWorkWith.getCourseProgress());
                        editCourseSpinner.setSelection(position);
                        editCourseSpinner.setEnabled(true);


                    }
                }
            });


        } else if (intent.hasExtra(KEY_TERM_ID)) {
            // New Course
            Integer term_id = extras.getInt(KEY_TERM_ID);
            buttonSaveUpdate.setText("Save");
            courseToWorkWith.setTerm_id(term_id);
            courseToWorkWith.setCourseProgress(CourseProgress.PLANNED);
            //set default

            int position = courseProgressArrayAdapter.getPosition(CourseProgress.PLANNED);
            editCourseSpinner.setSelection(position);
            editCourseSpinner.setEnabled(false);

            new Thread(new Runnable() {
                public void run() {
                    parentTerm = termViewModel.getTermById(term_id);
                }
            }).start();
        } else {
            System.out.println("NEW NOT FOR TERM");
        }
    }


    private void showDatePicker() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Set current Date to default
        DatePickerDialog dialog = new DatePickerDialog(CourseActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                DatePickerDialogListener,
                year, month, day);
        // That will allow only pick dates in range of the current term
        dialog.getDatePicker().setMinDate(parentTerm.getStart_date().getTime());
        dialog.getDatePicker().setMaxDate(parentTerm.getEnd_date().getTime());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
            intent.putExtra(KEY_TERM_ID, parentTerm.getTerm_id());
            startActivity(intent);
            finish();
            return true;
        } else if(item.getItemId() == R.id.view_assessments){
            Intent intent = new Intent(getApplicationContext(), AssesmentsForCourseActivity.class);
            intent.putExtra(KEY_COURSE_ID, courseToWorkWith.getCourse_id());
            startActivity(intent);
            finish();
            return true;
        } else if(item.getItemId() == R.id.delete_course){
            // TODO DELETE COURSE
            AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
            builder.setTitle("Delete Course?");
            builder.setMessage("Are you sure you want to delete course:  " + courseToWorkWith.getCourse_title());
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    courseViewModel.deleteCourse();
                    deleteNotificationSchedule(courseToWorkWith, "start");
                    deleteNotificationSchedule(courseToWorkWith, "end");
                    Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
                    intent.putExtra(KEY_TERM_ID, parentTerm.getTerm_id());
                    startActivity(intent);
                    finish();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();
            return true;
        }
        else if(item.getItemId() == R.id.notify_course){
            // TODO NOTIFY COURSE
            createNotificationSchedule("start");
            createNotificationSchedule("end");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * NOTIFICATIONS
    * */

    public void saveSharedConfigurations(String type, Course _courseToWorkWith, Integer counterId) {

        String COURSE_STRING_FOR_PREFS = String.format("COURSE_%s_%s",
                _courseToWorkWith.getCourse_id(), type);

        final SharedPreferences sharedPreferences =
                this.getSharedPreferences("dev.ddzmitry.studenttracker",
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(COURSE_STRING_FOR_PREFS, counterId)
                .apply();


        Toast.makeText(this, String.format("COURSE_STRING_FOR_PREFS %s", COURSE_STRING_FOR_PREFS)
                + sharedPreferences.getInt(COURSE_STRING_FOR_PREFS, 0), Toast.LENGTH_SHORT).show();

    }

    public void deleteNotificationSchedule(Course _courseToWorkWith, String type) {

        String COURSE_STRING_FOR_PREFS = String.format("COURSE_%s_%s", _courseToWorkWith.getCourse_id(), type);
        final SharedPreferences sharedPreferences =
                CourseActivity.this.getSharedPreferences("dev.ddzmitry.studenttracker",
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
        Intent intent_start = new Intent(CourseActivity.this, MessageReciever.class);
        intent_start.putExtra(NOTIFICATION_OBJECT, String.format("Course: %s ",courseToWorkWith.getCourse_title()));
        intent_start.putExtra(NOTIFICATION_OBJECT_ID, courseToWorkWith.getCourse_id());
        intent_start.putExtra(NOTIFICATION_DATE,
                type.equals("start") ? Utils.formatDate(courseToWorkWith.getCourse_start_date())
                        : Utils.formatDate(courseToWorkWith.getCourse_end_date()));
        intent_start.putExtra(NOTIFICATION_ALERT, type == "start" ? "starting" : "ending");
        intent_start.putExtra(NOTIFICATION_ALARM_ID, channel_counter_for_start);
        PendingIntent sender = PendingIntent.getBroadcast(CourseActivity.this, channel_counter_for_start, intent_start, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                type.equals("start") ? courseToWorkWith.getCourse_start_date().getTime() : courseToWorkWith.getCourse_end_date().getTime(),
                sender);
        // save in local memory
        saveSharedConfigurations(type, courseToWorkWith, channel_counter_for_start);
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

    /*
    * SMS
    * */
    public void smsSendMessage(String number) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse(
                String.format("smsto: %s",
                        number)));

        String smsBody = String.format("Course %s \n Note: \n %s",
                courseToWorkWith.getCourse_title(),
                editCourseNote.getText().toString());

        smsIntent.putExtra("sms_body", smsBody);

        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
        } else {
            Toast.makeText(CourseActivity.this, "Can't send message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
        intent.putExtra(KEY_TERM_ID, parentTerm.getTerm_id());
        startActivity(intent);
        finish();
    }

    public String ValidateCourse(Course courseToWorkWith) {

        if (courseToWorkWith.getCourse_title() == null) {
            return "Need Title for course.";
        } else if (courseToWorkWith.getCourse_start_date() == null) {
            return "Need Start Date for course.";

        } else if (courseToWorkWith.getCourse_end_date() == null) {
            return "Need End Date for course.";
        } else if (courseToWorkWith.getCourse_end_date()
                .compareTo(courseToWorkWith.getCourse_start_date()) < 0) {
            return "Course can't end be before it starts!";
        } else {
            return null;
        }

    }

    private void saveAndReturn() {
        String validator = ValidateCourse(courseToWorkWith);
        // course will not have id ....

        if (validator == null) {

            if (isUpdating) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Update Course?");
                builder.setMessage("Would you like to update course info?");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                        courseToWorkWith.setCourseProgress((CourseProgress) editCourseSpinner.getSelectedItem());
                        courseToWorkWith.setMentor(String.format("%s|%s|%s",
                                mentorToWorkWith.getFullName() != null ? mentorToWorkWith.getFullName() : "NA"
                                , mentorToWorkWith.getEmailAddress() != null ? mentorToWorkWith.getEmailAddress() : "NA"
                                , mentorToWorkWith.getPhone() != null ? mentorToWorkWith.getPhone() : "NA"));
                        courseToWorkWith.setNote(editCourseNote.getText().toString());
//

                        // check for if update needs for course
                        String TERM_STRING_FOR_PREFS =
                                String.format("COURSE_%s_%s", courseToWorkWith.getCourse_id(), "start");
                        final SharedPreferences sharedPreferences =
                                CourseActivity.this.getSharedPreferences("dev.ddzmitry.studenttracker",
                                        Context.MODE_PRIVATE);
                        if (sharedPreferences.getInt(TERM_STRING_FOR_PREFS, 0) != 0) {
                            // add menu ite
                            createNotificationSchedule("start");
                            createNotificationSchedule("end");
                        }


                        courseViewModel.saveCourse(courseToWorkWith);
                        // will keep all assessments even after course was updated

//                        assessmentViewModel.addOnCourseUpdates(assessmentsPerCourse);
//                        for (Assessment assessment : assessmentsPerCourse) {
//                            System.out.println("COURSE UPDATED SAVING ");
//                            assessmentViewModel.saveAssessment(assessment);
//                        }


                        Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
                        intent.putExtra(KEY_TERM_ID, parentTerm.getTerm_id());
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
                        intent.putExtra(KEY_TERM_ID, parentTerm.getTerm_id());
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Course?");
                builder.setMessage("Would you like to save " + courseToWorkWith.getCourse_title());
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        courseToWorkWith.setMentor(String.format("%s|%s|%s",
                                mentorToWorkWith.getFullName() != null ? mentorToWorkWith.getFullName() : "NA"
                                , mentorToWorkWith.getEmailAddress() != null ? mentorToWorkWith.getEmailAddress() : "NA"
                                , mentorToWorkWith.getPhone() != null ? mentorToWorkWith.getPhone() : "NA"));

                        courseToWorkWith.setNote(editCourseNote.getText().toString() );
                        courseViewModel.saveCourse(courseToWorkWith);
                        Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
                        intent.putExtra(KEY_TERM_ID, parentTerm.getTerm_id());
                        startActivity(intent);
//                    CourseActivity.super.onBackPressed();
                        finish();
                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
                        intent.putExtra(KEY_TERM_ID, parentTerm.getTerm_id());
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
}
