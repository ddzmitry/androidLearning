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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.ui.CourseAdapter;
import dev.ddzmitry.studenttracker.utilities.Constans;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;

public class ViewTermActivity extends AppCompatActivity {

    @BindView(R.id.coursesRecyclerView)
    RecyclerView coursesRecyclerView;

    // view
    private TermViewModel termViewModel;
    // courses
    private List<Course> coursesPerTerm = new ArrayList<>();
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

        }

    private void initViewModel() {
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
//        termViewModel.coursesPerTerm.observe(this,notesObserver);

        coursesPerTerm.addAll(termViewModel.coursesPerTerm.getValue());


        Bundle  extras = getIntent().getExtras();
        int term_id = extras.getInt(KEY_TERM_ID);
        termViewModel.loadTermData(term_id);

        Log.i("Termid", String.valueOf(term_id));
        System.out.println("TERM ID ID " + term_id);

        final Observer<List<Course>> notesObserver = new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> noteEntities) {
                coursesPerTerm.clear();
                coursesPerTerm.addAll(noteEntities);

                if(courseAdapter == null){

                    // Create adapter
                    courseAdapter = new CourseAdapter(coursesPerTerm,ViewTermActivity.this);
                    // Set Adapter to Display view
                    coursesRecyclerView.setAdapter(courseAdapter);
                } else {

                    courseAdapter.notifyDataSetChanged();
                }


            }
        };



        termViewModel.coursesPerTerm.observe(this,notesObserver);


    }

}
