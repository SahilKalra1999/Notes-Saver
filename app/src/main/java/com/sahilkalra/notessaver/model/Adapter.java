package com.sahilkalra.notessaver.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sahilkalra.notessaver.note.NoteDetails;
import com.sahilkalra.notessaver.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<String> titles;
    List<String> content;
    public Adapter(List<String> title, List<String> content){
        this.content=content;
        this.titles=title;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          holder.noteTitle.setText(titles.get(position));
          holder.noteContent.setText(content.get(position));
          int code=getRandomColor();
        holder.mCardView.setCardBackgroundColor(holder.view.getResources().getColor(code,null));
          holder.view.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i=new Intent(v.getContext(), NoteDetails.class);
                  i.putExtra("title", titles.get(position));
                  i.putExtra("content",content.get(position));
                  i.putExtra("code",code);
                  v.getContext().startActivity(i);
              }
          });
    }

    private int getRandomColor() {
        List<Integer> colorCode=new ArrayList<>();
        colorCode.add(R.color.blue1);
        colorCode.add(R.color.blue2);
        colorCode.add(R.color.orange1);
        colorCode.add(R.color.orange2);
        colorCode.add(R.color.red1);
        colorCode.add(R.color.yellow1);
        colorCode.add(R.color.red1);
        colorCode.add(R.color.red2);
        Random randomColor=new Random();
        int number=randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        View view;
        CardView mCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           noteTitle=itemView.findViewById(R.id.titles);
           noteContent=itemView.findViewById(R.id.content);
           mCardView=itemView.findViewById(R.id.noteCard);
           view=itemView;

        }
    }
}
