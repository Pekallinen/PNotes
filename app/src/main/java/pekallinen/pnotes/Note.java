package pekallinen.pnotes;

import java.util.Date;
import java.util.UUID;

public class Note {
    private UUID mUUID;
    private Date mDate;
    private String mTitle;
    private String mText;

    public Note(UUID id) {
        mUUID = id;
    }

    public Note(String title, String text) {
        mUUID = UUID.randomUUID();
        mDate = new Date();
        mTitle = title;
        mText = text;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
