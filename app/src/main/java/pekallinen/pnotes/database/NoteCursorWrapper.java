package pekallinen.pnotes.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import pekallinen.pnotes.Note;
import pekallinen.pnotes.database.NoteDbSchema.NoteTable;

public class NoteCursorWrapper extends CursorWrapper {
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        String text = getString(getColumnIndex(NoteTable.Cols.TEXT));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));

        Note note = new Note(UUID.fromString(uuidString));
        note.setTitle(title);
        note.setText(text);
        note.setDate(new Date(date));

        return note;
    }
}
