package com.beginning.notes;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import db.Note;

public class MainActivityViewModel extends AndroidViewModel {

   private int newNoteVisibility;
   private int deleteIconVisibility;
   private int editIconVisibility;
   private int cancelIconVisibility;

   private Note note ;
   private NoteRepository noteRepository;
   private LiveData<List<Note>> allNotes;
   private Note datasaver;
   private NotesAdapter.MyViewHolder myViewHolder;

   private int AddUpdateBtnText;




    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        newNoteVisibility = View.GONE;
        deleteIconVisibility = View.GONE;
        editIconVisibility = View.GONE;
        cancelIconVisibility = View.GONE;

        myViewHolder = null;

        note = new Note();

        datasaver = new Note();

        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();

        AddUpdateBtnText = R.string.buttonTextForAdd;


    }



    public Note getNote() {
        return note;
    }

    public int getNewNoteUIVisibility(){
        return newNoteVisibility;
    }

    public void setNewNoteUIVisibility(int visibility){
        newNoteVisibility =visibility;
    }

    public int getDeleteIconVisibility() {
        return deleteIconVisibility;
    }

    public void setDeleteIconVisibility(int deleteIconVisibility) {
        this.deleteIconVisibility = deleteIconVisibility;
    }

    public int getEditIconVisibility() {
        return editIconVisibility;
    }

    public void setEditIconVisibility(int editIconVisibility) {
        this.editIconVisibility = editIconVisibility;
    }

    public int getCancelIconVisibility() {
        return cancelIconVisibility;
    }

    public void setCancelIconVisibility(int cancelIconVisibility) {
        this.cancelIconVisibility = cancelIconVisibility;
    }

    public NotesAdapter.MyViewHolder getMyViewHolder() {
        return myViewHolder;
    }

    public void setMyViewHolder(NotesAdapter.MyViewHolder myViewHolder) {
        this.myViewHolder = myViewHolder;
    }

    public void insertNote(){
        datasaver.setIsThisNoteLongClicked(false);
        datasaver.setId(0);
        datasaver.setText(note.getText());
        noteRepository.insertNote(datasaver);



    }

    public void updateNote (String longClickedText){

        if(myViewHolder != null)
        datasaver.setId(allNotes.getValue().get(myViewHolder.getAdapterPosition()).getId());

        switch (longClickedText){

            case "onNoteLongClicked":
                datasaver.setIsThisNoteLongClicked(true);
                datasaver.setText(allNotes.getValue().get(myViewHolder.getAdapterPosition()).getText());
                break;

            case "onCancelIconClicked":

            case "onCancelButtonClicked":
                datasaver.setIsThisNoteLongClicked(false);
                datasaver.setText(allNotes.getValue().get(myViewHolder.getAdapterPosition()).getText());
                break;

            case "onUpdateButtonClicked":
                datasaver.setText(getNote().getText());
                datasaver.setIsThisNoteLongClicked(false);
                break;
        }


      if(myViewHolder != null )
        noteRepository.updateNote(datasaver);
    }

    public void deleteNote(){
        if(myViewHolder != null) {

            Note deleteNote = allNotes.getValue().get(myViewHolder.getAdapterPosition());
            noteRepository.deleteNote(deleteNote);
        }

    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }


    public void setAddUpdateBtnText(int buttonTextForUpdate) {
        AddUpdateBtnText  = buttonTextForUpdate;
    }

    public int getAddUpdateBtnText(){
        return AddUpdateBtnText;
    }
}
