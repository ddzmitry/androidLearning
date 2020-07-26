package dev.ddzmitry.studenttracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.ui.CourseAdapter;
import dev.ddzmitry.studenttracker.ui.TasksAdapter;
import dev.ddzmitry.studenttracker.utilities.SampleData;
import dev.ddzmitry.studenttracker.view.CourseViewModel;
import dev.ddzmitry.studenttracker.view.TaskViewModel;
import dev.ddzmitry.studenttracker.view.TermViewModel;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;

public class MainActivity extends AppCompatActivity {

    // Use butterKnife
    @BindView(R.id.recycler_view)
    RecyclerView TermRecyclerView;
    @BindView(R.id.fab_main)
    FloatingActionButton add_term;


    private List<Term> allTerms = new ArrayList<>();
    private TasksAdapter tasksAdapter;
    private TermViewModel termViewModel;
    private TaskViewModel taskViewModel;
//    private

//    private void alertView( String message ) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        dialog.setTitle( "Hello" )
//                .setIcon(R.drawable.ic_launcher)
//                .setMessage(message)
////     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////      public void onClick(DialogInterface dialoginterface, int i) {
////          dialoginterface.cancel();
////          }})
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialoginterface, int i) {
//                    }
//                }).show();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Bind View
        ButterKnife.bind(this);
        // For performance boost
        initRecyclerView();
        initViewModel();
        // Adding term
        add_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MainActivity", "Adding new term");
                Intent intent = new Intent(getApplicationContext(), TermActivity.class);
                startActivity(intent);
            }
        });

//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("Alert message to be shown");
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
    }

    private void initRecyclerView() {
        // Set Size to be the same
        TermRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        TermRecyclerView.setLayoutManager(layoutManager);
        // instanciate adapter
        tasksAdapter = new TasksAdapter(allTerms, this);
        TermRecyclerView.setAdapter(tasksAdapter);

    }

    private void initViewModel() {

        final Observer<List<Term>> termsObserver = new Observer<List<Term>>() {
            @Override
            public void onChanged(@Nullable List<Term> terms) {
                allTerms.clear();
                allTerms.addAll(terms);

                if (tasksAdapter == null) {
                    // Create adapter
                    tasksAdapter = new TasksAdapter(allTerms, MainActivity.this);
                    // Set Adapter to Display view
                    TermRecyclerView.setAdapter(tasksAdapter);
                } else {
                    // notify if data has changed
                    tasksAdapter.notifyDataSetChanged();
                }

            }
        };
        // pass class for view
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.allTerms.observe(this, termsObserver);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_data){
            deleteAllData();
        } else  if (id == R.id.action_get_summary){
            Intent intent = new Intent(this,SummaryActivity.class);
            startActivity(intent);
//            System.out.println("Getting Summary");
        }

        return super.onOptionsItemSelected(item);
    }

    private void addSampleData() {
        taskViewModel.addSampleData();
    }
    private void deleteAllData() {
        taskViewModel.removeAllData();
    }

}
