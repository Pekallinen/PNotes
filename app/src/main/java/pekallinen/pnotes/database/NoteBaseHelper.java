package pekallinen.pnotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pekallinen.pnotes.database.NoteDbSchema.NoteTable;

public class NoteBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "pnotes.db";

    public NoteBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NoteTable.Cols.UUID + ", " +
                NoteTable.Cols.TITLE + ", " +
                NoteTable.Cols.TEXT + ", " +
                NoteTable.Cols.DATE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Implement database upgrade
    }
}
