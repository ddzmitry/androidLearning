package dev.ddzmitry.plainolnotes3;
import butterknife.OnClick;
import dev.ddzmitry.plainolnotes3.database.NoteEntity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.plainolnotes3.ui.NotesAdapter;
import dev.ddzmitry.plainolnotes3.utilities.SampleData;
import dev.ddzmitry.plainolnotes3.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView myRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler(){
        Intent intent = new Intent(this,EditorActivity.class);
        startActivity(intent);

    }

    private List<NoteEntity> notesDate = new ArrayList<>();
    private NotesAdapter mAdapter;
    private MainViewModel mViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

//  REPLACED WITH BUTTERKNIFE AT LINE 32
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        // Adding data from ViewController
//        notesDate.addAll(mViewModel.mNotes);
//        for (NoteEntity noteEntity : notesDate) {
//
//            Log.i("PlainOlNotes",noteEntity.toString());
//
//        }
    }

    private void initViewModel() {


        final Observer<List<NoteEntity>> notesObserver = new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntity> noteEntities) {
                notesDate.clear();
                notesDate.addAll(noteEntities);

                if(mAdapter == null){

                    // Create adapter
                    mAdapter = new NotesAdapter(notesDate,MainActivity.this);
                    // Set Adapter to Display view
                    myRecyclerView.setAdapter(mAdapter);
                } else {

                    mAdapter.notifyDataSetChanged();
                }


            }
        };

        // pass class for view
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // subscribed to data
        mViewModel.mNotes.observe(this,notesObserver);

    }

    private void initRecyclerView() {

        myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(layoutManager);
        // Add line
        DividerItemDecoration divider = new DividerItemDecoration(myRecyclerView.getContext(),layoutManager.getOrientation());
        myRecyclerView.addItemDecoration(divider);

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
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all){
            removeAllData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void removeAllData() {

        mViewModel.removeAllData();
    }

    private void addSampleData() {
        Log.i("addSampleData", "CALLED");
        mViewModel.addSampleData();
    }
}
