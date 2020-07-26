package dev.ddzmitry.studenttracker;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;
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

    // Calendar
    private DatePickerDialog.OnDateSetListener DatePickerDialogListener;
    // Bools
    Boolean isUpdating;
    Boolean editingStart;
    // Term
    private Term termToWorkWith = new Term();


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
                // month + 1

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
//                    editingStart = false;


            }
        };


        UtilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termViewModel.saveTerm(termToWorkWith);
            }
        });


    }

    private void initViewModel() {


        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

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

        Bundle extras = getIntent().getExtras();
        if (extras == null) {

            System.out.println("NEW TERM");
            UtilityButton.setText(String.valueOf("Create"));

        } else {

            setNotToUpdate();
            // Use to pull up data
            UtilityButton.setText(String.valueOf("Update"));
            int term_id = extras.getInt(KEY_TERM_ID);
            Log.i("TermActivity", "TermId: " + term_id);
            termViewModel.loadTermData(term_id);
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

        // TODO check if there are coursers , and if there are courses then dont delete , else delete it

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
        } else if (id == R.id.view_all_courses)
        {
            Log.i("TermActivity", "Going to check courses " + termToWorkWith.toString());
            //Intent intent = new Intent(mContext, CoursesForTermActivity.class);
            Intent intent = new Intent(getApplicationContext(), CoursesForTermActivity.class);
            intent.putExtra(KEY_TERM_ID,
                    termToWorkWith.getTerm_id());
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
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
