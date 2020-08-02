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
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.ui.CourseAdapter;
import dev.ddzmitry.studenttracker.utilities.Utils;
import dev.ddzmitry.studenttracker.view.CourseViewModel;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.GLOBAL_COUNTER_CHANNELS;
import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_ALARM_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_ALERT;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_DATE;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_OBJECT;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_OBJECT_ID;
import static dev.ddzmitry.studenttracker.utilities.Utils.formatDate;

public class TermActivity extends AppCompatActivity {

    // Butter
    @BindView(R.id.editTermText)
    TextView editTermText;
    @BindView(R.id.editTermStartDate)
    TextView editTermStartDate;
    @BindView(R.id.editTermEndDate)
    TextView editTermEndDate;
    @BindView(R.id.editTermUtilityButton)
    Button UtilityButton;
    // view
    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    // data
    private List<Course> coursesPerTerm = new ArrayList<>();
    // Calendar
    private DatePickerDialog.OnDateSetListener DatePickerDialogListener;
    // Bools
    Boolean isUpdating;
    // Term
    private Term termToWorkWith = new Term();

    private Executor executor = Executors.newSingleThreadExecutor();
    // Use for local storage
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);
        ButterKnife.bind(this);
        // arrow btn
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new Thread(new Runnable() {
            public void run() {

                initViewModel();
            }
        }).start();

        DatePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                System.out.println(String.format("%s/%s/%s", year, month, day));
                Date date = new GregorianCalendar(year, month, day).getTime();
                Date end_date;
                Calendar myCal = Calendar.getInstance();
                myCal.setTime(date);
                myCal.add(Calendar.MONTH, +6);
                end_date = myCal.getTime();
                editTermStartDate.setText(formatDate(date));
                editTermEndDate.setText(formatDate(end_date));
                termToWorkWith.setStart_date(date);
                termToWorkWith.setEnd_date(end_date);
            }
        };


        UtilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validation =  ValidateTerm(termToWorkWith);
                if(validation==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(TermActivity.this);
                    builder.setTitle("Update/Save Term?");
                    builder.setMessage("Would you like to save/update term info?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            termViewModel.saveTerm(termToWorkWith);
                            courseViewModel.addOnTermUpdates(coursesPerTerm);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TermActivity.this);
                    builder.setTitle("Validation Error");
                    builder.setMessage(validation);
                    builder.show();

                }



            }
        });

    }

    // Disable Menu Options
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            menu.clear();
        }
        return true;
}

    private void initViewModel() {
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        // Observing the data
        termViewModel.liveTermData.observe(this, new Observer<Term>() {
            @Override
            public void onChanged(@Nullable Term term) {
                if (term != null) {
                    editTermText.setText(term.getTerm_title());
                    editTermStartDate.setText(formatDate(term.getStart_date()));
                    editTermEndDate.setText(formatDate(term.getEnd_date()));
                    termToWorkWith = termViewModel.liveTermData.getValue();
                    Log.i("TermActivity", "Working with Term: " + termToWorkWith.toString());
                }

            }
        });


        final Observer<List<Course>> termsObserver = new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {

                if (courses == null) {
                    System.out.println("COURSES ARE NULL");
                } else {
                    coursesPerTerm.clear();
                    coursesPerTerm.addAll(courses);
                }
            }
        };

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setToUpdate();
            UtilityButton.setText(String.valueOf("Create"));

        } else {
            setNotToUpdate();
            // Use to pull up data
            UtilityButton.setText(String.valueOf("Update"));
            int term_id = extras.getInt(KEY_TERM_ID);
            Log.i("TermActivity", "TermId: " + term_id);
            courseViewModel.getCoursesByTerm(term_id);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    termViewModel.loadTermData(term_id);
                    courseViewModel
                            .getCoursesByTerm(term_id)
                            .observe(TermActivity.this, termsObserver);
                }
            });
        }


    }

    public void setNotToUpdate() {
        UtilityButton.setVisibility(View.INVISIBLE);
        editTermText.setEnabled(false);
    }

    public void setToUpdate() {
        isUpdating = true;
        UtilityButton.setVisibility(View.VISIBLE);
        editTermText.setEnabled(true);
        editTermStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        editTermEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TermActivity.this, "Sets automatically! \n Start Date + 6 month.", Toast.LENGTH_LONG).show();
            }
        });


        editTermText
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
                            Toast.makeText(TermActivity.this, "NEED TO HAVE VALUE", Toast.LENGTH_SHORT).show();
                        } else {
                            termToWorkWith.setTerm_title(editable.toString());
                        }
                    }
                });
    }

    public void deleteTerm() {

        if (coursesPerTerm.size() == 0) {

            // allert
            AlertDialog.Builder builder = new AlertDialog.Builder(TermActivity.this);
            builder.setTitle("Delete term?");
            builder.setMessage("Are you sure you want to delete term:  " + termToWorkWith.getTerm_title());
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    termViewModel.deleteTerm();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Validation Error");
            builder.setMessage("Remove all courses from term! Before Deleting it!");
            builder.show();

        }

    }

    public String ValidateTerm(Term termToWorkWith) {
        if (termToWorkWith.getTerm_title() == null) {
            return "Need Title for term.";
        } else if (termToWorkWith.getStart_date() == null) {
            return "Need Start Date for term.";
        } else if (termToWorkWith.getEnd_date() == null) {
            return "Need End Date for term.";
        } else {
            return null;
        }
    }


    // Menue
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.term_mentu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_term) {
            setToUpdate();
            return true;
        } else if (id == R.id.delete_term) {

            deleteTerm();

        } else if (id == R.id.activate_term) {
        }
        else if (id == R.id.view_all_courses) {
            Log.i("TermActivity", "Going to check courses " + termToWorkWith.toString());
            //Intent intent = new Intent(mContext, CoursesForTermActivity.class);
            Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
            intent.putExtra(KEY_TERM_ID,
                    termToWorkWith.getTerm_id());
            startActivity(intent);
            finish();


        }
        else if (id == R.id.notify_term){
            // for Start Term
            createNotificationSchedule("start");
            createNotificationSchedule("end");


            // FOR END TERM
//            Integer channel_counter_for_end = getGlobalChannelCounter();
//            Intent intent_end=new Intent(TermActivity.this,MessageReciever.class);
//            intent_end.putExtra(NOTIFICATION_OBJECT, "Term");
//            intent_end.putExtra(NOTIFICATION_OBJECT_ID, termToWorkWith.getTerm_id());
//            intent_end.putExtra(NOTIFICATION_DATE, Utils.formatDate(termToWorkWith.getStart_date()));
//            intent_end.putExtra(NOTIFICATION_ALERT, "ending");
//            intent_start.putExtra(NOTIFICATION_ALARM_ID, channel_counter_for_end);
//            sender= PendingIntent.getBroadcast(TermActivity.this,channel_counter_for_end,intent_end,0);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, termToWorkWith.getEnd_date().getTime(), sender);
//            updateGlobalChannelCounter(channel_counter_for_end);

            // for cancel
//            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,REQUEST_CODE,intent,PendingIntent.FLAG_NO_CREATE);
//
//            if (pendingIntent!=null)
//                alarmManager.cancel(pendingIntent);

        }


        return super.onOptionsItemSelected(item);
    }

    public void createNotificationSchedule(String type){

        Integer channel_counter_for_start = getGlobalChannelCounter();
        Intent intent_start=new Intent(TermActivity.this,MessageReciever.class);
        intent_start.putExtra(NOTIFICATION_OBJECT, "Term");
        intent_start.putExtra(NOTIFICATION_OBJECT_ID, termToWorkWith.getTerm_id());
        intent_start.putExtra(NOTIFICATION_DATE, Utils.formatDate(termToWorkWith.getStart_date()));
        intent_start.putExtra(NOTIFICATION_ALERT, type == "start" ? "starting" : "ending");
        intent_start.putExtra(NOTIFICATION_ALARM_ID, channel_counter_for_start);

        PendingIntent sender= PendingIntent.getBroadcast(TermActivity.this,channel_counter_for_start,intent_start,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                type == "start" ? termToWorkWith.getStart_date().getTime(): termToWorkWith.getEnd_date().getTime(),
                sender);
        updateGlobalChannelCounter(channel_counter_for_start);

    }


    public Integer getGlobalChannelCounter (){
        final SharedPreferences sharedPreferences = this.getSharedPreferences("dev.ddzmitry.studenttracker", Context.MODE_PRIVATE);
        // if it keeps incrementing
        System.out.println("getGlobalChannelCounter " + sharedPreferences.getInt(GLOBAL_COUNTER_CHANNELS,0));

        return sharedPreferences.getInt(GLOBAL_COUNTER_CHANNELS,0);
    }
    // for channels
    public void updateGlobalChannelCounter (Integer curr){

        System.out.println("updateGlobalChannelCounter CURRENT IS " + curr);
                final SharedPreferences sharedPreferences = this
                .getSharedPreferences("dev.ddzmitry.studenttracker",
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(GLOBAL_COUNTER_CHANNELS,curr+1).apply();

        Toast.makeText(this,
                "UPDATING  GLOBAL_COUNTER_CHANNELS TO " + sharedPreferences.getInt(GLOBAL_COUNTER_CHANNELS,0), Toast.LENGTH_SHORT).show();
    }



    private void showDatePicker() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(TermActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                DatePickerDialogListener,
                year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


}
