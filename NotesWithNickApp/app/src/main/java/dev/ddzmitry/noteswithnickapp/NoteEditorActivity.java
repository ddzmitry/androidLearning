package dev.ddzmitry.noteswithnickapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = findViewById(R.id.editText);

        Intent intent = getIntent();

        noteId  = intent.getIntExtra("noteId",-1);

        if(noteId != -1){
            // get note
            editText.setText(MainActivity.notes.get(noteId));

        } else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() -1;
        }

        // on text changed
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // change text in Main veiew
                MainActivity.notes.set(noteId,String.valueOf(charSequence));
                // Update data
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
