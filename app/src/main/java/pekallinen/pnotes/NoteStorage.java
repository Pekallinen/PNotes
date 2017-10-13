package pekallinen.pnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pekallinen.pnotes.database.NoteBaseHelper;
import pekallinen.pnotes.database.NoteCursorWrapper;
import pekallinen.pnotes.database.NoteDbSchema.NoteTable;

public class NoteStorage {
    private static NoteStorage sNoteStorage;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NoteStorage get(Context context) {
        if(sNoteStorage == null) {
            sNoteStorage = new NoteStorage(context);
        }
        return sNoteStorage;
    }

    private NoteStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext).getWritableDatabase();
    }

    public void addNote(Note note) {
        ContentValues values = getContentValues(note);
        mDatabase.insert(NoteTable.NAME, null, values);
    }

    public void removeNote(Note note) {
        String uuidString = note.getUUID().toString();
        mDatabase.delete(NoteTable.NAME, NoteTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper cursor = queryNotes(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return notes;
    }

    public Note getNote(UUID id) {
        NoteCursorWrapper cursor = queryNotes(NoteTable.Cols.UUID + " = ?",
                new String[] { id.toString() });

        try {
            if(cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNote();
        }
        finally {
            cursor.close();
        }
    }

    public void updateNote(Note note) {
        String uuidString = note.getUUID().toString();
        ContentValues values = getContentValues(note);
        mDatabase.update(NoteTable.NAME, values, NoteTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID, note.getUUID().toString());
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.TEXT, note.getText());
        values.put(NoteTable.Cols.DATE, note.getDate().getTime());

        return values;
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null, // Null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new NoteCursorWrapper(cursor);
    }
}
