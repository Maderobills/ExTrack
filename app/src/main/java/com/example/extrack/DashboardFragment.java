package com.example.extrack;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton fab_main_btn, fab_income_btn,fab_expense_btn;
    private TextView fab_income_text,fab_expense_text;

    private boolean isOpen = false;

    private Animation FadeOpen, FadeClose;

    private FirebaseAuth mAuth;
   // private DatabaseReference mIncomeData, mExpenseData;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        //ncomeData = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
       // mExpenseData = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);


        fab_main_btn = myView.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myView.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myView.findViewById(R.id.expense_ft_btn);

        fab_income_text = myView.findViewById(R.id.income_ft_text);
        fab_expense_text = myView.findViewById(R.id.expense_ft_text);

        FadeOpen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();

                if (isOpen){
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_text.startAnimation(FadeClose);
                    fab_expense_text.startAnimation(FadeClose);
                    fab_income_text.setClickable(false);
                    fab_expense_text.setClickable(false);
                    isOpen = false;

                }else {
                    fab_income_btn.startAnimation(FadeOpen);
                    fab_expense_btn.startAnimation(FadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_text.startAnimation(FadeOpen);
                    fab_expense_text.startAnimation(FadeOpen);
                    fab_income_text.setClickable(true);
                    fab_expense_text.setClickable(true);
                    isOpen = true;
                }

            }
        });

        return myView;

    }

    public void ftAnimation(){

        if (isOpen){
            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_text.startAnimation(FadeClose);
            fab_expense_text.startAnimation(FadeClose);
            fab_income_text.setClickable(false);
            fab_expense_text.setClickable(false);
            isOpen = false;

        }else {
            fab_income_btn.startAnimation(FadeOpen);
            fab_expense_btn.startAnimation(FadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_text.startAnimation(FadeOpen);
            fab_expense_text.startAnimation(FadeOpen);
            fab_income_text.setClickable(true);
            fab_expense_text.setClickable(true);
            isOpen = true;
        }
    }

    private void addData(){

        //INCOME

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incomeDataInsert();

            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseDataInsert();

            }
        });

    }

    public void incomeDataInsert(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myviewm);
        AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        EditText edtAmount = myviewm.findViewById(R.id.amount_edit);
        EditText edtType = myviewm.findViewById(R.id.type_edit);
        EditText edtNote= myviewm.findViewById(R.id.note_edit);

        Button btnSave = myviewm.findViewById(R.id.btn_add);
        Button btnCancel = myviewm.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = edtAmount.getText().toString().trim();
                String type = edtType.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(amount)){
                    edtAmount.setError("Specify Amount");
                    return;
                }

                if (TextUtils.isEmpty(type)){
                    edtType.setError("Specify (Salary,Client,Tip..etc)");
                    return;
                }

                int ouramountint = Integer.parseInt(amount);

                if (TextUtils.isEmpty(note)){
                    edtNote.setError("Description here!!!");
                    return;
                }



                Toast.makeText(getActivity(), "Income Data Added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void expenseDataInsert(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        View myview = inflator.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);

        AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        EditText edtAmount = myview.findViewById(R.id.amount_edit);
        EditText edtType = myview.findViewById(R.id.type_edit);
        EditText edtNote= myview.findViewById(R.id.note_edit);

        Button btnSave = myview.findViewById(R.id.btn_add);
        Button btnCancel = myview.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmAmount = edtAmount.getText().toString().trim();
                String tmType = edtType.getText().toString().trim();
                String tmNote = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmount)){
                    edtAmount.setError("Specify Amount");
                    return;
                }

                int inamount = Integer.parseInt(tmAmount);

                if (TextUtils.isEmpty(tmType)){
                    edtType.setError("Specify (Food,Transport,Entertainment,Tip..etc)");
                    return;
                }

                if (TextUtils.isEmpty(tmNote)){
                    edtNote.setError("Description here!!!");
                    return;
                }



                Toast.makeText(getActivity(), "Expense Data Added", Toast.LENGTH_SHORT).show();


                ftAnimation();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ftAnimation();
                dialog.dismiss();

            }
        });

        dialog.show();

    }

}