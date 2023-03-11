package com.example.extrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrack.Model.Data;

import java.util.ArrayList;

public class AdapterExpense extends RecyclerView.Adapter<AdapterExpense.MyViewHolder> {

    ArrayList<Data> arrayList;
    Context context;

    public AdapterExpense(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.expense_recycler_data,parent,false);
       return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Data data = arrayList.get(position);
        holder.type.setText(data.getType());
        holder.amount.setText(String.valueOf(data.getAmount()));
        holder.note.setText(data.getNote());
        holder.date.setText(data.getDate());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView type, note, id, date,amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type_txt_expense);
            date = itemView.findViewById(R.id.date_txt_expense);
            note = itemView.findViewById(R.id.note_txt_expense);
            amount = itemView.findViewById(R.id.amount_txt_expense);
        }
    }
}
