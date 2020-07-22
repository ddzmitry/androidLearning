package dev.ddzmitry.studenttracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.ui.CourseAdapter;
import dev.ddzmitry.studenttracker.ui.TasksAdapter;
import dev.ddzmitry.studenttracker.utilities.Constans;
import dev.ddzmitry.studenttracker.view.CourseViewModel;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;

public class ViewTermActivity extends AppCompatActivity {

    @BindView(R.id.coursesRecyclerView)
    RecyclerView coursesRecyclerView;

    @BindView(R.id.termTitleText)
    TextView termTitleText;

    // view
    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    // courses
    private List<Course> coursesPerTerm = new ArrayList<>();
    private Term term;
    // adapter
    private CourseAdapter courseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

        private void initRecyclerView() {

            coursesRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            coursesRecyclerView.setLayoutManager(layoutManager);
            // Add line
            DividerItemDecoration divider = new DividerItemDecoration(coursesRecyclerView.getContext(),layoutManager.getOrientation());
            coursesRecyclerView.addItemDecoration(divider);

            final CourseAdapter courseAdapter = new CourseAdapter(coursesPerTerm,this);
            coursesRecyclerView.setAdapter(courseAdapter);

        }

    private void initViewModel() {




        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
//        courseViewModel.addSampleData();


        termViewModel.liveTermData.observe(this, new Observer<Term>() {
            @Override
            public void onChanged(@Nullable Term term) {
                if(term != null){
                    termTitleText.setText(term.getTerm_title());
                }

            }
        });



//


        final Observer<List<Course>> termsObserver = new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {

                if(courses == null){
                    System.out.println("COURSES ARE NULL");
                }
//                System.out.println(courses.toArray().toString());
                coursesPerTerm.clear();
                coursesPerTerm.addAll(courses);

                if (courseAdapter == null) {
                    // Create adapter
                    courseAdapter = new CourseAdapter(coursesPerTerm, ViewTermActivity.this);
                    // Set Adapter to Display view
                    coursesRecyclerView.setAdapter(courseAdapter);
                } else {
                    // notify if data has changed
                    courseAdapter.notifyDataSetChanged();
                }

            }
        };


        Bundle  extras = getIntent().getExtras();
        if(extras == null){
            System.out.println("NEW TERM");
        } else {
            int term_id = extras.getInt(KEY_TERM_ID);
            termViewModel.loadTermData(term_id);
//            courseViewModel.getCoursesByTerm(term_id);
            courseViewModel
                    .getCoursesByTerm(term_id)
                    .observe(ViewTermActivity.this, termsObserver);

        }




    }

}
