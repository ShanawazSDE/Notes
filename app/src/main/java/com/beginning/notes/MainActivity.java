package com.beginning.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beginning.notes.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import db.Note;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    ImageView deleteIcon,cancelIcon,editIcon;
    MainActivityViewModel mainActivityViewModel;
    InputMethodManager imm;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    boolean isItemAdded;
    boolean isAnyNoteLongClicked;//to decide scroll to top in recycler view , scroll only if item is added;




    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        activityMainBinding.etNote.setMaxHeight((int) (metrics.heightPixels * 0.4));
        activityMainBinding.btnAddUpdate.setText(mainActivityViewModel.getAddUpdateBtnText());
        activityMainBinding.newNoteLayout.setVisibility(mainActivityViewModel.getNewNoteUIVisibility());
        activityMainBinding.setNote(mainActivityViewModel.getNote());
        ////
        isAnyNoteLongClicked = false;
        MainClickHandlers mainClickHandlers = new MainClickHandlers();
        activityMainBinding.setMainClickHandlers(mainClickHandlers);

        recyclerView = activityMainBinding.recyclerView;
        isItemAdded = false;
        notesAdapter = new NotesAdapter(mainClickHandlers.noteLongClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(notesAdapter);

        mainActivityViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                notesAdapter.updateRecyclerView((ArrayList<Note>) notes);

                if(isItemAdded) {
                    recyclerView.scrollToPosition(0);
                    isItemAdded = false;


                }
                }

        });



//////////////////////find delete icon and cancel icon so that they can be set visible/gone in NOTELONGCLICK in MAINCLICKHANDLERS///////////////////////
         deleteIcon = findViewById(R.id.deleteIcon);
         cancelIcon= findViewById(R.id.closeIcon);
         editIcon = findViewById(R.id.editIcon);

         deleteIcon.setVisibility(mainActivityViewModel.getDeleteIconVisibility());
         cancelIcon.setVisibility(mainActivityViewModel.getCancelIconVisibility());
         editIcon.setVisibility(mainActivityViewModel.getEditIconVisibility());
         //////////////////////////////////////////////////////////////////////////////////
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //////to restore onLongClick changes when screen orientation changes///////////

       // 2//viewHolder = mainActivityViewModel.getMyViewHolder();

    }




    public class MainClickHandlers{

      public  View.OnLongClickListener noteLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (activityMainBinding.newNoteLayout.getVisibility() != View.VISIBLE && mainActivityViewModel.getMyViewHolder() == null){
//checking viewHolder to see whether some note had been long clicked if so prevent long click of some other note
// if the new Note Layout is visible then the user is probably adding or editing
// some note so prevent the long click of some other note


                    mainActivityViewModel.setMyViewHolder((NotesAdapter.MyViewHolder) view.getTag());
                    isAnyNoteLongClicked = true;


                if (deleteIcon.getVisibility() == View.GONE) {
                    deleteIcon.setVisibility(View.VISIBLE);
                    mainActivityViewModel.setDeleteIconVisibility(View.VISIBLE);
                }

                if (cancelIcon.getVisibility() == View.GONE) {
                    cancelIcon.setVisibility(View.VISIBLE);
                    mainActivityViewModel.setCancelIconVisibility(View.VISIBLE);
                }

                if (editIcon.getVisibility() == View.GONE) {
                    editIcon.setVisibility(View.VISIBLE);
                    mainActivityViewModel.setEditIconVisibility(View.VISIBLE);
                }
                if(cancelIcon.getVisibility() == View.VISIBLE || deleteIcon.getVisibility() == View.VISIBLE
                || editIcon.getVisibility() == View.VISIBLE) {

                    if(mainActivityViewModel.getMyViewHolder() != null) {
                        mainActivityViewModel.updateNote("onNoteLongClicked");
                    }

                }

            }
                return true;
            }
        };


        public void onFabClicked(View view){

            if( activityMainBinding.newNoteLayout.getVisibility() == View.GONE && !isAnyNoteLongClicked){
                activityMainBinding.btnAddUpdate.setText(R.string.buttonTextForAdd);
                mainActivityViewModel.setAddUpdateBtnText(R.string.buttonTextForAdd);
            activityMainBinding.setNote(mainActivityViewModel.getNote());
            activityMainBinding.newNoteLayout.setVisibility(View.VISIBLE);
            mainActivityViewModel.setNewNoteUIVisibility(View.VISIBLE);

            }

        }

        public void onEditIconClicked(View view){

            hideCancelDeleteEditIcons();

            if( activityMainBinding.newNoteLayout.getVisibility() == View.GONE){


                activityMainBinding.newNoteLayout.setVisibility(View.VISIBLE);
                mainActivityViewModel.setNewNoteUIVisibility(View.VISIBLE);
                activityMainBinding.btnAddUpdate.setText(R.string.buttonTextForUpdate);
                mainActivityViewModel.setAddUpdateBtnText(R.string.buttonTextForUpdate);
                activityMainBinding.etNote.setText(mainActivityViewModel.getAllNotes().getValue()
                        .get(mainActivityViewModel.getMyViewHolder().getAdapterPosition()).getText());
            }
        }


        public void onDeleteIconClicked(View view){
            hideCancelDeleteEditIcons();


            mainActivityViewModel.deleteNote();
            mainActivityViewModel.setMyViewHolder(null);
            isAnyNoteLongClicked =false;
        }

        public void onCancelIconClicked(View view){
            hideCancelDeleteEditIcons();


            mainActivityViewModel.updateNote("onCancelIconClicked");
            mainActivityViewModel.setMyViewHolder(null);

            isAnyNoteLongClicked = false;


        }

        private void hideCancelDeleteEditIcons() {
            if(deleteIcon.getVisibility() == View.VISIBLE) {
                deleteIcon.setVisibility(View.GONE);
                mainActivityViewModel.setDeleteIconVisibility(View.GONE);
            }

            if(cancelIcon.getVisibility() == View.VISIBLE) {
                cancelIcon.setVisibility(View.GONE);
                mainActivityViewModel.setCancelIconVisibility(View.GONE);
            }

            if(editIcon.getVisibility() == View.VISIBLE) {
                editIcon.setVisibility(View.GONE);
                mainActivityViewModel.setEditIconVisibility(View.GONE);
            }
        }


        public void onCancelButtonClicked(View view){

           activityMainBinding.newNoteLayout.setVisibility(View.GONE);
           mainActivityViewModel.setNewNoteUIVisibility(View.GONE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            activityMainBinding.etNote.getText().clear();

            if(mainActivityViewModel.getMyViewHolder() != null) {
                mainActivityViewModel.updateNote("onCancelButtonClicked");
                mainActivityViewModel.setMyViewHolder(null);
            }

        isAnyNoteLongClicked = false;

        }
        public void onAddIconClicked(View view){


            if( activityMainBinding.newNoteLayout.getVisibility() == View.GONE && !isAnyNoteLongClicked){
                activityMainBinding.btnAddUpdate.setText(R.string.buttonTextForAdd);
                activityMainBinding.setNote(mainActivityViewModel.getNote());
                activityMainBinding.newNoteLayout.setVisibility(View.VISIBLE);
                mainActivityViewModel.setNewNoteUIVisibility(View.VISIBLE);
            }
        }

        public void onAddUpdateButtonClicked(View view) {

            if (activityMainBinding.etNote.getText().toString().length() == 0) {
                Toast.makeText(MainActivity.this, "Note shouldn't be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Button button = (Button) view;
            if (button.getText().equals("add")) {

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mainActivityViewModel.insertNote();
                isItemAdded = true;
                activityMainBinding.newNoteLayout.setVisibility(View.GONE);
                mainActivityViewModel.setNewNoteUIVisibility(View.GONE);

                activityMainBinding.etNote.getText().clear();




            }
            
            else if (button.getText().equals( "update")){
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                activityMainBinding.newNoteLayout.setVisibility(View.GONE);
                mainActivityViewModel.setNewNoteUIVisibility(View.GONE);
                mainActivityViewModel.updateNote("onUpdateButtonClicked");
                activityMainBinding.etNote.getText().clear();


                mainActivityViewModel.setMyViewHolder(null);



            }

            isAnyNoteLongClicked = false;
        }
        
  
    }

    @Override
    public void onBackPressed() {
        //this is used to unhiglight the note when back is pressed
        super.onBackPressed();
        if(mainActivityViewModel.getMyViewHolder() != null){
            mainActivityViewModel.updateNote("onCancelButtonClicked");
        }
    }
}