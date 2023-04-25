package com.example.extrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrack.Model.Data;
import com.example.extrack.Model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.MyViewHolder> {

    ArrayList<Note> arrayList;
    Context context;

    private EditText  editNote,editTitle;
    private Button btnUpdate,btnDelete;
    private String title,note,post_key;
    private DatabaseReference mNotesData;

    public AdapterNotes(ArrayList<Note> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.note_recycler_data,parent,false);
       return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        final Note data = arrayList.get(position);
        holder.title.setText(data.getTitle());
        holder.note.setText(data.getNote());
       // holder.date.setText(data.getDate());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post_key=data.getId();

                title = data.getTitle();
                note = data.getNote();

                updateDataItem();
            }
        });

    }

    private void updateDataItem() {

        AlertDialog.Builder mydialog=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View myview=inflater.inflate(R.layout.updte_notes,null);
        mydialog.setView(myview);

        final AlertDialog dialog=mydialog.create();

        editTitle=myview.findViewById(R.id.title_edit);
        editNote=myview.findViewById(R.id.note_edit);

        editTitle.setText(title);
        editTitle.setSelection(title.length());

        editNote.setText(note);
        editNote.setSelection(note.length());


        btnUpdate=myview.findViewById(R.id.btn_update);
        btnDelete=myview.findViewById(R.id.btn_delete);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //arrayList.clear();
                title=editTitle.getText().toString().trim();
                note=editNote.getText().toString().trim();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Note data = new Note(title,note,post_key,mDate);


                mNotesData= FirebaseDatabase.getInstance().getReference().child("NotesData").child(FirebaseAuth.getInstance().getUid());

                mNotesData.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context,"Updated Successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNotesData= FirebaseDatabase.getInstance().getReference().child("NotesData").child(FirebaseAuth.getInstance().getUid());

                mNotesData.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,"Failed to Delete"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                dialog.dismiss();
            }
        });

        dialog.show();

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, note, id, date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_txt_note);
            note = itemView.findViewById(R.id.note_txt_note);
        }
    }
}
