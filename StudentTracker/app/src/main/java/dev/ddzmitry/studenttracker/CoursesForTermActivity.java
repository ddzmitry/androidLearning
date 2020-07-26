package dev.ddzmitry.studenttracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.ui.CourseAdapter;
import dev.ddzmitry.studenttracker.view.CourseViewModel;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;
import static dev.ddzmitry.studenttracker.utilities.Utils.formatDate;

public class CoursesForTermActivity extends AppCompatActivity {

    @BindView(R.id.coursesRecyclerView)
    RecyclerView coursesRecyclerView;

    @BindView(R.id.termTitleText)
    TextView termTitleText;

    @BindView(R.id.termDateText)
    TextView termDateText;

    // view
    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    // courses
    private List<Course> coursesPerTerm = new ArrayList<>();
    private Term term;
    // adapter
    private CourseAdapter courseAdapter;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courses_per_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        new Thread(new Runnable() {
            public void run() {
                initRecyclerView();
                initViewModel();
            }
        }).start();

        // arrow btn
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_mentu, menu);
        // hide show different things in the menu
//        MenuItem item = menu.findItem(R.id.action_add_data);
//        item.setVisible(false);
        return true;
    }

    private void initRecyclerView() {

        coursesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(layoutManager);
        // Add line
        DividerItemDecoration divider = new DividerItemDecoration(coursesRecyclerView.getContext(), layoutManager.getOrientation());
        coursesRecyclerView.addItemDecoration(divider);

        final CourseAdapter courseAdapter = new CourseAdapter(coursesPerTerm, this);
        coursesRecyclerView.setAdapter(courseAdapter);

    }


    private void initViewModel() {


        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
//        courseViewModel.addSampleData();


        termViewModel.liveTermData.observe(this, new Observer<Term>() {
            @Override
            public void onChanged(@Nullable Term term) {
                if (term != null) {
                    termTitleText.setText(term.getTerm_title());

                    termDateText.setText(String.format("Start: %s End: %s", formatDate(term.getStart_date()), formatDate(term.getEnd_date())));
                }

            }
        });


//


        final Observer<List<Course>> termsObserver = new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {

                if (courses == null) {
                    System.out.println("COURSES ARE NULL");
                } else {
                    coursesPerTerm.clear();
                    coursesPerTerm.addAll(courses);
                }
                if (courseAdapter == null) {
                    // Create adapter
                    courseAdapter = new CourseAdapter(coursesPerTerm, CoursesForTermActivity.this);
                    // Set Adapter to Display view
                    coursesRecyclerView.setAdapter(courseAdapter);
                } else {
                    // notify if data has changed
                    courseAdapter.notifyDataSetChanged();
                }

            }
        };


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            System.out.println("NEW TERM");
        } else {
            int term_id = extras.getInt(KEY_TERM_ID);
            Log.i("CoursesForTermActivity", "TERM ID is :" + term_id);
//            courseViewModel.addSampleData();
            courseViewModel.getCoursesByTerm(term_id);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    termViewModel.loadTermData(term_id);
                    courseViewModel
                            .getCoursesByTerm(term_id)
                            .observe(CoursesForTermActivity.this, termsObserver);
                }
            });


        }


    }


}
