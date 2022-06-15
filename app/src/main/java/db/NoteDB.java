package db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDB extends RoomDatabase {

    public abstract NoteDao getNoteDao();
}
