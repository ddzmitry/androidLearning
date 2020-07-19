package dev.ddzmitry.plainolnotes3;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.plainolnotes3.database.NoteEntity;
import dev.ddzmitry.plainolnotes3.viewmodel.EditorViewModel;

import static dev.ddzmitry.plainolnotes3.utilities.Constants.EDITING_KEY;
import static dev.ddzmitry.plainolnotes3.utilities.Constants.NOTE_ID_KEY;

public class EditorActivity extends AppCompatActivity {

    @BindView(R.id.note_text)
    TextView mTextView;


    private EditorViewModel mViewModel;
    private boolean mNewNote, mEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set Icon
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ButterKnife.bind(this);
        if(savedInstanceState != null){
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();

    }

    private void initViewModel() {
        mViewModel = ViewModelProviders
                .of(this).get(EditorViewModel.class);

        mViewModel.mLiveNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                if (noteEntity != null && !mEditing) {
                    mTextView.setText(noteEntity.getText());
                }

            }


        });

        Bundle  extras = getIntent().getExtras();
        if(extras == null){
            setTitle("New Note");
            mNewNote = true;
        } else {
            setTitle("Edit Note");
            int noteId = extras.getInt(NOTE_ID_KEY);
            mViewModel.loadData(noteId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // only when existing load
        if(!mNewNote){
            // show trashcan
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            saveAndReturn();
            return true;
        }
        else if(item.toString().equals("Delete")){
            mViewModel.deleteNote();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();

    }

    private void saveAndReturn() {
        mViewModel.saveNote(mTextView.getText().toString());
        // to finish activity
        finish();
    }
    // Device changes orientation

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY,true);
        super.onSaveInstanceState(outState);
    }
}
