package db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    public void insertNote(Note note);

    @Update
    public void updateNote(Note note);

    @Query("select * from Note order by id desc")
    public LiveData<List<Note>> getAllNotes();

    @Delete
    public void deleteNote(Note note);
}
