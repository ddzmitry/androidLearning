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

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.database.CourseProgress;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Mentor;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.view.CourseViewModel;
import dev.ddzmitry.studenttracker.view.MentorViewModel;
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

    @BindView(R.id.fab_delete_course)
    FloatingActionButton fab_delete_course;

    @BindView(R.id.editCourseSpinner)
    Spinner editCourseSpinner;

    @BindView(R.id.editCourseMentorEmail)
    EditText editCourseMentorEmail;

    @BindView(R.id.editCourseMentorName)
    EditText editCourseMentorName;

    @BindView(R.id.editCourseNote)
    EditText editCourseNote;

    // View Models
    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;
    // Classes
    private Mentor mentorToWorkWith = new Mentor();
    private Course courseToWorkWith = new Course();
    private Term parentTerm = new Term();
    private DatePickerDialog.OnDateSetListener DatePickerDialogListener;

    Boolean isUpdating = false;
    Boolean editingStart, editingEnd;
    // for spinner
    private ArrayAdapter<CourseProgress> courseProgressArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        initViewModel();

        fab_delete_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                builder.setTitle("Delete Course?");
                builder.setMessage("Are you sure you want to delete course:  " + courseToWorkWith.getCourse_title());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        courseViewModel.deleteCourse();
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

    }

    private void initSpinner() {
        courseProgressArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CourseProgress.values());
        editCourseSpinner.setAdapter(courseProgressArrayAdapter);
    }



    private void initViewModel() {
        initSpinner();
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();

        if (intent.hasExtra(KEY_COURSE_ID)) {

            fab_delete_course.setVisibility(View.VISIBLE);
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

                        editCourseText.setText(course.getCourse_title().toString());
                        editCourseStartDate.setText(formatDate(course.getCourse_start_date()));
                        editCourseEndDate.setText(formatDate(course.getCourse_end_date()));

                        // Using ArrayList
                        if(course.getMentor() !=null)
                        {
                            List<String> arrParams = new ArrayList<String>();
                            String str[] = course.getMentor().split("\\|");
                            arrParams = Arrays.asList(str);
                            mentorToWorkWith.setFullName(arrParams.get(0) != null ? arrParams.get(0): "");
                            mentorToWorkWith.setEmailAddress(arrParams.get(1) != null ? arrParams.get(1): "");
                            editCourseMentorEmail.setText(mentorToWorkWith.getEmailAddress());
                            editCourseMentorName.setText(mentorToWorkWith.getFullName());
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
        // only when existing load
//        if(!mNewNote){
//            // show trashcan
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.menu_editor,menu);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
            intent.putExtra(KEY_TERM_ID, parentTerm.getTerm_id());
            startActivity(intent);
            finish();
            return true;
        } else if (item.toString().equals("Delete")) {
//            mViewModel.deleteNote();
            finish();
        }

        return super.onOptionsItemSelected(item);
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
                        courseToWorkWith.setMentor(String.format("%s|%s",mentorToWorkWith.getFullName(),mentorToWorkWith.getEmailAddress()));

                        courseToWorkWith.setNote(editCourseNote.getText().toString());

                        courseViewModel.saveCourse(courseToWorkWith);

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
                        courseToWorkWith.setMentor(String.format("%s|%s",mentorToWorkWith.getFullName(),mentorToWorkWith.getEmailAddress()));
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
