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

public class IncomeAdaptor extends RecyclerView.Adapter<IncomeAdaptor.ViewHolder> {

    ArrayList<Data> arrayListIncome;
    Context contextIncome;

    public IncomeAdaptor(ArrayList<Data> arrayListIncome, Context contextIncome) {
        this.arrayListIncome = arrayListIncome;
        this.contextIncome = contextIncome;
    }

    @NonNull
    @Override
    public IncomeAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextIncome).inflate(R.layout.income_recycler_data_dash,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeAdaptor.ViewHolder holder, int position) {

        String gH = "GHS ";

        Data data = arrayListIncome.get(position);
        holder.type.setText(data.getType());
        holder.amount.setText(String.format(gH+"%.2f",data.getAmount()));
        holder.note.setText(data.getNote());
        holder.date.setText(data.getDate());

        }


    @Override
    public int getItemCount() {
       return arrayListIncome.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView type, note, id, date,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type_txt_income_dash);
            date = itemView.findViewById(R.id.date_txt_income_dash);
            note = itemView.findViewById(R.id.note_txt_income_dash);
            amount = itemView.findViewById(R.id.amount_txt_income_dash);
        }
    }
}
