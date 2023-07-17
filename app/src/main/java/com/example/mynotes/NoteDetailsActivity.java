package com.example.mynotes;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn,deleteNote;
    TextView pageTitle;
    String title, content, docId;
    boolean isUpdateNote = false;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitle = findViewById(R.id.page_title);
        deleteNote = findViewById(R.id.delete_note);
        //get data from intent
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        //check if data is null
        if (docId != null && !docId.isEmpty()) {
            isUpdateNote = true;
            titleEditText.setText(title);
            contentEditText.setText(content);
        }
        //change title if not add new
        if (isUpdateNote) {
            pageTitle.setText("Edit your note");
            deleteNote.setVisibility(View.VISIBLE);
        }
        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = titleEditText.getText().toString();
                String noteContent = contentEditText.getText().toString();
                if (noteTitle == null || noteTitle.isEmpty()) {
                    titleEditText.setError("Title is required!");
                    return;
                }
                Note note = new Note();
                note.setTitle(noteTitle);
                note.setContent(noteContent);
                note.setTimestamp(Timestamp.now());

                saveNoteToFireBase(note);
            }
        });
//        saveNoteBtn.setOnClickListener((v -> saveNote()));
        //delete
        builder = new AlertDialog.Builder(this);
        deleteNote.setOnClickListener((v -> setAlert()));
    }


    //    void saveNote(){
//        String noteTitle = titleEditText.getText().toString();
//        String noteContent = contentEditText.getText().toString();
//        if (noteTitle.trim().isEmpty()) {
//            titleEditText.setText("No title");
//            return;
//        }
//        Note note = new Note();
//        note.setTitle(noteTitle);
//        note.setContent(noteContent);
//        note.setTimestamp(Timestamp.now());
//
//        saveNoteToFireBase(note);
//    }
    void saveNoteToFireBase(Note note) {
        DocumentReference documentReference;
        //check if update mode
        if (isUpdateNote == true) {
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // note is added
                    if (isUpdateNote == true) {
                        Utility.showToast(NoteDetailsActivity.this, "Note update successfully!");
                    } else {
                        Utility.showToast(NoteDetailsActivity.this, "Note added successfully!");
                    }
                    finish();
                } else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding note!");
                }
            }
        });
    }

    private void deleteNoteInFireBase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //check if suscess
                if (task.isSuccessful()) {
                    Utility.showToast(NoteDetailsActivity.this, "Note is deleted");
                    finish();
                } else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed to delete");
                }
            }
        });
    }

    // alert before delete
    private void setAlert() {
        builder.setTitle("Alert!!").setMessage("Do you want to delete this note");
        builder.setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNoteInFireBase();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}

