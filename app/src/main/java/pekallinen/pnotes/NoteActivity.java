package pekallinen.pnotes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

import static pekallinen.pnotes.MainActivity.EXTRA_NOTE_ID;

public class NoteActivity extends AppCompatActivity {
    private Note mNote;
    private EditText mTitle;
    private EditText mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mTitle = (EditText) findViewById(R.id.note_title);
        mText = (EditText) findViewById(R.id.note_text);

        Button mSaveButton = (Button) findViewById(R.id.note_button_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTitle.getText().length() == 0) {
                    Toast.makeText(NoteActivity.this, R.string.toast_empty_note, Toast.LENGTH_SHORT).show();
                }
                else {
                    if(mNote != null) {
                        mNote.setTitle(mTitle.getText().toString());
                        mNote.setText(mText.getText().toString());
                        mNote.setDate(new Date());
                        NoteStorage.get(NoteActivity.this).updateNote(mNote);
                    }
                    else {
                        mNote = new Note(mTitle.getText().toString(), mText.getText().toString());
                        NoteStorage.get(NoteActivity.this).addNote(mNote);
                    }
                    onBackPressed();
                }

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.note_toolbar);
        toolbar.setTitle(R.string.new_note);

        // Get the note if the activity was started from the list
        Bundle b = getIntent().getExtras();
        if(b != null) {
            UUID id = (UUID) b.getSerializable(EXTRA_NOTE_ID);
            mNote = NoteStorage.get(NoteActivity.this).getNote(id);
            mTitle.setText(mNote.getTitle());
            mText.setText(mNote.getText());
            mSaveButton.setText(R.string.note_button_save_changes);
            toolbar.setTitle(R.string.edit_note);
        }

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_note:
                if(mNote != null) {
                    // Confirm delete
                    new AlertDialog.Builder(this)
                            .setTitle("Confirm delete")
                            .setMessage("Are you sure?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    NoteStorage.get(NoteActivity.this).removeNote(mNote);
                                    onBackPressed();
                                }
                            }).show();
                }
                else {
                    // Just go back when deleting a non-saved note
                    onBackPressed();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
