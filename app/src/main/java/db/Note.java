package db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Note implements Comparable {

    @PrimaryKey(autoGenerate = true)
     int id;
     String text;
     boolean isThisNoteLongClicked = false;

    @Ignore
    public Note() {
    }


    public Note(int id , String text, boolean isThisNoteLongClicked ) {
        this.id = id;
        this.text = text;
        this.isThisNoteLongClicked = isThisNoteLongClicked;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean  getIsThisNoteLongClicked() {
        return isThisNoteLongClicked;
    }

    public void setIsThisNoteLongClicked(boolean thisNoteLongClicked) {
        isThisNoteLongClicked = thisNoteLongClicked;
    }

    @Override
    public String toString() {
        return ""+this.id+" "+this.text+" "+this.isThisNoteLongClicked;
    }

    @Override
    public int compareTo(Object o) {
        Note note = (Note) o;

        return (this.text.equals(note.text) && this.isThisNoteLongClicked == note.isThisNoteLongClicked)?1:0 ;
    }
}
