package com.beginning.notes;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import db.Note;
import db.NoteDB;

public class NoteRepository {

   private NoteDB noteDB;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        noteDB = Room.databaseBuilder(application, NoteDB.class,"noteDB").build();
        allNotes = noteDB.getNoteDao().getAllNotes();
    }

    public void insertNote(Note note){

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDB.getNoteDao().insertNote(note);

    }
        });
    }

    public void updateNote(Note note){

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDB.getNoteDao().updateNote(note);

            }
        });
    }


    public void deleteNote(Note deleteNote){

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDB.getNoteDao().deleteNote(deleteNote);

            }
        });
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
}