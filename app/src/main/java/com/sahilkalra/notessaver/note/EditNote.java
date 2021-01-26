package com.sahilkalra.notessaver.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sahilkalra.notessaver.MainActivity;
import com.sahilkalra.notessaver.R;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {
    Intent data;
    EditText editNoteContent, editNoteTitle;
    FirebaseFirestore fStore;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar=findViewById(R.id.toolbar);
        spinner=findViewById(R.id.progressBar2);
        setSupportActionBar(toolbar);
        editNoteContent=findViewById(R.id.editNoteContent);
        editNoteTitle=findViewById(R.id.editNoteTitle);
        data=getIntent();
        String noteTitle=data.getStringExtra("title");
        String noteContent=data.getStringExtra("content");
        editNoteTitle.setText(noteTitle);
        editNoteContent.setText(noteContent);
        fStore=FirebaseFirestore.getInstance();
        FloatingActionButton saveEditedNote = findViewById(R.id.saveEditedNote);
        saveEditedNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTitle=editNoteTitle.getText().toString();
                String nContent=editNoteContent.getText().toString();
                if (nTitle.isEmpty() || nContent.isEmpty()){
                    Toast.makeText(EditNote.this, "Can not Save note with empty field.", Toast.LENGTH_SHORT).show();
                    return;
                }
                spinner.setVisibility(View.VISIBLE);
                //save note
                DocumentReference docref=fStore.collection("notes").document(data.getStringExtra("noteId2"));
                Map<String,Object> note=new HashMap<>();
                note.put("title",nTitle);
                note.put("content",nContent);
                docref.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditNote.this, "Note Saved.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNote.this, "Error, Try again..", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }
}