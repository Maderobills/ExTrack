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

public class DebtAdaptor extends RecyclerView.Adapter<DebtAdaptor.ViewHolder> {

    ArrayList<Data> arrayListDebt;
    Context contextDebt;

    public DebtAdaptor(ArrayList<Data> arrayListDebt, Context contextDebt) {
        this.arrayListDebt= arrayListDebt;
        this.contextDebt = contextDebt;
    }

    @NonNull
    @Override
    public DebtAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextDebt).inflate(R.layout.debt_recycler_data_dash,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtAdaptor.ViewHolder holder, int position) {

        String gH = "GHS -";

        Data data = arrayListDebt.get(position);
        holder.paymethod.setText(data.getPaymethod());
        holder.type.setText(data.getType());
        holder.amount.setText(String.format(gH+"%.2f",data.getAmount()));
        holder.note.setText(data.getNote());
        holder.date.setText(data.getDate());

        }


    @Override
    public int getItemCount() {
       return arrayListDebt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView paymethod,type, note, id, date,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            paymethod = itemView.findViewById(R.id.payment_txt_debt_dash);
            type = itemView.findViewById(R.id.type_txt_debt_dash);
            date = itemView.findViewById(R.id.date_txt_debt_dash);
            note = itemView.findViewById(R.id.note_txt_debt_dash);
            amount = itemView.findViewById(R.id.amount_txt_debt_dash);
        }
    }
}
