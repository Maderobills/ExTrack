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

public class ExpenseAdaptor extends RecyclerView.Adapter<ExpenseAdaptor.ViewHolder> {

    ArrayList<Data> arrayListExpense;
    Context contextExpense;

    public ExpenseAdaptor(ArrayList<Data> arrayListExpense, Context contextExpense) {
        this.arrayListExpense = arrayListExpense;
        this.contextExpense = contextExpense;
    }

    @NonNull
    @Override
    public ExpenseAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextExpense).inflate(R.layout.expense_recycler_data_dash,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdaptor.ViewHolder holder, int position) {

        Data data = arrayListExpense.get(position);
        holder.type.setText(data.getType());
        holder.amount.setText(String.valueOf(data.getAmount()));
        holder.note.setText(data.getNote());
        holder.date.setText(data.getDate());

        }


    @Override
    public int getItemCount() {
       return arrayListExpense.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView type, note, id, date,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type_txt_expense_dash);
            date = itemView.findViewById(R.id.date_txt_expense_dash);
            note = itemView.findViewById(R.id.note_txt_expense_dash);
            amount = itemView.findViewById(R.id.amount_txt_expense_dash);
        }
    }
}
