package com.beginning.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {


    private ArrayList<Note> notesList = new ArrayList<>();
    View.OnLongClickListener noteLongClickListener;

    public NotesAdapter(View.OnLongClickListener noteLongClickListener) {
        this.noteLongClickListener = noteLongClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_view, parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Note note = notesList.get(position);



        if(note.getIsThisNoteLongClicked()){
            holder.noteText.setBackgroundResource(R.drawable.list_item_long_click_drawable);
            holder.noteText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.textColorLight));
        }

        holder.noteText.setText(note.getText());
        holder.noteText.setTag(holder);
        holder.noteText.setOnLongClickListener(noteLongClickListener);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {

       if(payloads.isEmpty()) {
           super.onBindViewHolder(holder, position, payloads);
       }
       else {
           Bundle bundle = (Bundle) payloads.get(0);
          for(String key: bundle.keySet()){
              if (key.equals("newNote")){
                  holder.noteText.setText(bundle.getString("newNote"));
              }
              else if(key.equals("isThisNoteLongClicked")){
                  boolean isThisNoteLongClicked = bundle.getBoolean("isThisNoteLongClicked");
                  if(isThisNoteLongClicked){
                      holder.noteText.setBackgroundResource(R.drawable.list_item_long_click_drawable);
                      holder.noteText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                              R.color.textColorLight));
                  }
                  else{
                      holder.noteText.setBackgroundResource(R.drawable.list_item_drawable);
                      holder.noteText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                              R.color.black));
                  }
              }
          }
       }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    protected class MyViewHolder extends RecyclerView.ViewHolder{
        TextView noteText ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.tv_note);
        }
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
//        this method is used to undo the changes made to viewholder b4 deleting i.e changing
//        border to list_item_drawable from list_item_long_click_drawable
    super.onViewRecycled(holder);
        holder.noteText.setBackgroundResource(R.drawable.list_item_drawable);
        holder.noteText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                R.color.black));
    }

    public void updateRecyclerView(ArrayList<Note> newList){
        DiffUtil.DiffResult  diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallBack(notesList, newList));
        diffResult.dispatchUpdatesTo(this);
        notesList.clear();
        notesList.addAll(newList);

    }


}
