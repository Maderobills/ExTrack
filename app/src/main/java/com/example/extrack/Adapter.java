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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter extends  RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<Data> arrayList;
    Context context;

    private EditText editAmount, editNote,editType;
    private Button btnUpdate,btnDelete;

    private String type,note,post_key;
    private float amount;

    private DatabaseReference mIncomeData;



    public Adapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.income_recycler_data,parent,false);
       return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String gH = "GHS ";

        final Data data = arrayList.get(position);
        holder.type.setText(data.getType());
        holder.amount.setText(String.format(gH+"%.2f",data.getAmount()));
        holder.note.setText(data.getNote());
        holder.date.setText(data.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post_key=data.getId();

                type = data.getType();
                note = data.getNote();
                amount = data.getAmount();

                updateDataItem();
            }
        });

    }

    private void updateDataItem() {

        AlertDialog.Builder mydialog=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View myview=inflater.inflate(R.layout.updte_data,null);
        mydialog.setView(myview);

        final AlertDialog dialog=mydialog.create();

        editAmount=myview.findViewById(R.id.amount_edit);
        editType=myview.findViewById(R.id.type_edit);
        editNote=myview.findViewById(R.id.note_edit);

        editType.setText(type);
        editType.setSelection(type.length());

        editNote.setText(note);
        editNote.setSelection(note.length());

        editAmount.setText(String.valueOf(amount));
        editAmount.setSelection(String.valueOf(amount).length());

        btnUpdate=myview.findViewById(R.id.btn_update);
        btnDelete=myview.findViewById(R.id.btn_delete);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //arrayList.clear();
                type=editType.getText().toString().trim();
                note=editNote.getText().toString().trim();
                String maomunt=String.valueOf(amount);
                maomunt=editAmount.getText().toString().trim();
                float myAmount=Float.parseFloat(maomunt);
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(myAmount,type,note,post_key,mDate);


                mIncomeData= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(FirebaseAuth.getInstance().getUid());

                mIncomeData.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                mIncomeData= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(FirebaseAuth.getInstance().getUid());

                mIncomeData.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView type, note, id, date,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type_txt_income);
            date = itemView.findViewById(R.id.date_txt_income);
            note = itemView.findViewById(R.id.note_txt_income);
            amount = itemView.findViewById(R.id.amount_txt_income);
        }
    }


}
