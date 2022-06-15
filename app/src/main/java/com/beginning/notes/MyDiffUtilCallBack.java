package com.beginning.notes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

import db.Note;

public class MyDiffUtilCallBack extends DiffUtil.Callback {

    ArrayList<Note> oldList;
    ArrayList<Note> newList;

    public MyDiffUtilCallBack(ArrayList<Note> oldList, ArrayList<Note> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return (oldList!=null)?oldList.size():0;
    }

    @Override
    public int getNewListSize() {
        return (newList!=null)?newList.size():0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = oldList.get(oldItemPosition).compareTo(newList.get(newItemPosition));

        return result>0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        Note oldNote = oldList.get(oldItemPosition);
        Note newNote = newList.get(newItemPosition);

        Bundle bundle = new Bundle();
        if(! oldNote.getText().equals(newNote.getText())){
            bundle.putString("newNote", newNote.getText());
        }
        if( oldNote.getIsThisNoteLongClicked() != newNote.getIsThisNoteLongClicked()){
            bundle.putBoolean("isThisNoteLongClicked", newNote.getIsThisNoteLongClicked());
        }
        return (bundle.size()>0)?bundle:null;
    }

}
