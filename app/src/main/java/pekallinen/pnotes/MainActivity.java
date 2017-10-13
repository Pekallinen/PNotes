package pekallinen.pnotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_NOTE_ID = "note_id";

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add a divider between recyclerview items
        DividerItemDecoration noteSeparator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(noteSeparator);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_note:
                startActivity(new Intent(this, NoteActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        NoteStorage noteStorage = NoteStorage.get(this);
        List<Note> notes = noteStorage.getNotes();

        if(mAdapter == null) {
            mAdapter = new NoteAdapter(notes);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setNotes(notes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Note mNote;

        private TextView mTitle;
        private TextView mDate;


        public NoteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.list_item_title);
            mDate = (TextView) itemView.findViewById(R.id.list_item_date);
        }

        public void bindNote(Note note) {
            mNote = note;
            mTitle.setText(note.getTitle());
            mDate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(note.getDate()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            UUID id = mNote.getUUID();
            intent.putExtra(EXTRA_NOTE_ID, id);
            startActivity(intent);
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_note, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {
            Note note = mNotes.get(position);
            holder.bindNote(note);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        public void setNotes(List<Note> notes) {
            mNotes = notes;
        }
    }
}
