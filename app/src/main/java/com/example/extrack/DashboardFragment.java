package com.example.extrack;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extrack.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
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

    private FloatingActionButton fab_main_btn, fab_income_btn,fab_expense_btn,fab_debt_btn;
    private TextView fab_income_text,fab_expense_text,fab_debt_text;
    private TextView totalIncome, totalExpense, totalRemain, totalDebt;
    private TextView percentRemain, percentExpense, percentDebt;

    private RecyclerView incomeRecycler, expenseRecycler,debtRecycler;

    IncomeAdaptor adaptorIncome;
    ExpenseAdaptor adaptorExpense;
    DebtAdaptor adaptorDebt;
    ArrayList<Data> list,listE,listD;

    private boolean isOpen = false;

    private Animation FadeOpen, FadeClose;

    private FirebaseAuth mAuth;
   private DatabaseReference mIncomeData, mExpenseData,mDebtData, allRef;


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

        mIncomeData = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseData = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        mDebtData = FirebaseDatabase.getInstance().getReference().child("DebtData").child(uid);
        allRef = FirebaseDatabase.getInstance().getReference();

        fab_main_btn = myView.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myView.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myView.findViewById(R.id.expense_ft_btn);
        fab_debt_btn = myView.findViewById(R.id.debt_ft_btn);

        fab_income_text = myView.findViewById(R.id.income_ft_text);
        fab_expense_text = myView.findViewById(R.id.expense_ft_text);
        fab_debt_text = myView.findViewById(R.id.debt_ft_text);

        incomeRecycler = myView.findViewById(R.id.recycler_id_income_dash);
        expenseRecycler = myView.findViewById(R.id.recycler_id_expense_dash);
        debtRecycler = myView.findViewById(R.id.recycler_id_debt_dash);

        totalIncome = myView.findViewById(R.id.income_set_result);
        totalExpense = myView.findViewById(R.id.expense_set_result);
        totalRemain = myView.findViewById(R.id.total_set_result);
        totalDebt = myView.findViewById(R.id.total_debt_result);
        percentExpense = myView.findViewById(R.id.total_exp_percent);
        percentRemain = myView.findViewById(R.id.total_set_percent);
        //percentDebt = myView.findViewById(R.id.total_debt_percent);


        FadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();

                if (isOpen) {
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_debt_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);
                    fab_debt_btn.setClickable(false);

                    fab_income_text.startAnimation(FadeClose);
                    fab_expense_text.startAnimation(FadeClose);
                    fab_debt_text.startAnimation(FadeClose);
                    fab_income_text.setClickable(false);
                    fab_expense_text.setClickable(false);
                    fab_debt_text.setClickable(false);
                    isOpen = false;

                } else {
                    fab_income_btn.startAnimation(FadeOpen);
                    fab_expense_btn.startAnimation(FadeOpen);
                    fab_debt_btn.startAnimation(FadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);
                    fab_debt_btn.setClickable(true);

                    fab_income_text.startAnimation(FadeOpen);
                    fab_expense_text.startAnimation(FadeOpen);
                    fab_debt_text.startAnimation(FadeOpen);
                    fab_income_text.setClickable(true);
                    fab_expense_text.setClickable(true);
                    fab_debt_text.setClickable(true);
                    isOpen = true;
                }
            }
        });


        allRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                mExpenseData.addValueEventListener(new ValueEventListener() {

                    float totalSumE = 0;
                    final String gH = "GHS ";
                    final String gHs = "GHS -";

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot mysnap : snapshot.getChildren()) {
                            Data data = mysnap.getValue(Data.class);

                            totalSumE += data.getAmount();


                            String stResultE = String.format("%.2f", totalSumE);

                            totalExpense.setText(gH + stResultE);
                        }
                        mIncomeData.addValueEventListener(new ValueEventListener() {

                            float totalSum = 0;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                for (DataSnapshot mysnap : snapshot.getChildren()) {
                                    Data data = mysnap.getValue(Data.class);

                                    totalSum += data.getAmount();

                                    String stResult = String.format("%.2f", totalSum);

                                    totalIncome.setText(gH + stResult);


                                }
                                mDebtData.addValueEventListener(new ValueEventListener() {

                                    float debtSum = 0;

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        for (DataSnapshot mysnap : snapshot.getChildren()) {
                                            Data data = mysnap.getValue(Data.class);

                                            debtSum += data.getAmount();

                                            String stDResult = String.format("%.2f",debtSum);

                                            totalDebt.setText(gHs + stDResult);


                                        }

                                        float remSum = totalSum - totalSumE;
                                        String stRem = String.format("%.2f", remSum);
                                        totalRemain.setText(gH + stRem);
                                        int perExp = (int) (100 * totalSumE / totalSum);
                                        int perRem = (int) (100 * remSum / totalSum);
                                       // int perDebt = (int) ( debtSum / remSum);
                                       // int debtRem = 100 - perDebt;
                                        if (remSum > 0) {
                                            String stPerExp = String.valueOf(perExp);
                                            String stPerRem = String.valueOf(perRem);
                                            percentExpense.setText(stPerExp + "%");
                                            percentRemain.setText(stPerRem + "%");

                                          /*  if ( debtSum > remSum) {
                                                String stPerDebt = String.valueOf(debtRem);
                                                percentDebt.setText(stPerDebt + "%");
                                            } else {
                                                percentDebt.setText(100 + "%");
                                            }*/
                                        } else if (remSum < 0) {
                                            float addDebt =- remSum;
                                            float newDebt = addDebt + debtSum;
                                            //int perDebt2 = (int) ( newDebt / remSum);
                                           // int debtRem2 = 100 - perDebt2;

                                            percentExpense.setText(100 + "%");
                                            String stnDResult = String.format("%.2f",newDebt);
                                            totalDebt.setText(gHs + stnDResult);

                                            //String stPerDebt2 = String.valueOf(debtRem2);

                                            //percentDebt.setText(stPerDebt2 + "%");
                                        } else {
                                            percentExpense.setText(100 + "%");
                                        }


                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }


                                });

                            }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }


                                });

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });


                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                incomeRecycler.setHasFixedSize(true);
                incomeRecycler.setLayoutManager(layoutManager);

                LinearLayoutManager layoutManageExpenser = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                layoutManageExpenser.setReverseLayout(true);
                layoutManageExpenser.setStackFromEnd(true);
                expenseRecycler.setHasFixedSize(true);
                expenseRecycler.setLayoutManager(layoutManageExpenser);

                LinearLayoutManager layoutManageDebt = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                layoutManageDebt.setReverseLayout(true);
                layoutManageDebt.setStackFromEnd(true);
                debtRecycler.setHasFixedSize(true);
                debtRecycler.setLayoutManager(layoutManageDebt);

                list = new ArrayList<>();
                adaptorIncome = new IncomeAdaptor(list, getContext());
                incomeRecycler.setAdapter(adaptorIncome);

                listE = new ArrayList<>();
                adaptorExpense = new ExpenseAdaptor(listE, getContext());
                expenseRecycler.setAdapter(adaptorExpense);

                listD = new ArrayList<>();
                adaptorDebt = new DebtAdaptor(listD, getContext());
                debtRecycler.setAdapter(adaptorDebt);

                mIncomeData.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Data data = dataSnapshot.getValue(Data.class);
                            list.add(data);

                        }
                        adaptorIncome.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mExpenseData.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listE.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Data data = dataSnapshot.getValue(Data.class);
                            listE.add(data);

                        }
                        adaptorExpense.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mDebtData.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listD.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Data data = dataSnapshot.getValue(Data.class);
                            listD.add(data);

                        }
                        adaptorDebt.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                return myView;
            }


    public void ftAnimation(){

        if (isOpen){
            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_debt_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);
            fab_debt_btn.setClickable(false);

            fab_income_text.startAnimation(FadeClose);
            fab_expense_text.startAnimation(FadeClose);
            fab_debt_text.startAnimation(FadeClose);
            fab_income_text.setClickable(false);
            fab_expense_text.setClickable(false);
            fab_debt_text.setClickable(false);
            isOpen = false;

        }else {
            fab_income_btn.startAnimation(FadeOpen);
            fab_expense_btn.startAnimation(FadeOpen);
            fab_debt_btn.startAnimation(FadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);
            fab_debt_btn.setClickable(true);

            fab_income_text.startAnimation(FadeOpen);
            fab_expense_text.startAnimation(FadeOpen);
            fab_debt_text.startAnimation(FadeOpen);
            fab_income_text.setClickable(true);
            fab_expense_text.setClickable(true);
            fab_debt_text.setClickable(true);
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

        fab_debt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               debtDataInsert();

            }
        });

    }

    public void incomeDataInsert(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myviewm);
        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        final Spinner methodSpinner = myviewm.findViewById(R.id.payment_edit);
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.paytypes));
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSpinner.setAdapter(methodAdapter);

        final Spinner typesSpinner = myviewm.findViewById(R.id.type_edit);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.typesIn));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typesSpinner.setAdapter(typeAdapter);

        final EditText edtAmount = myviewm.findViewById(R.id.amount_edit);
        final EditText edtNote= myviewm.findViewById(R.id.note_edit);

        Button btnSave = myviewm.findViewById(R.id.btn_add);
        Button btnCancel = myviewm.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = edtAmount.getText().toString().trim();
                String tmMethod = methodSpinner.getSelectedItem().toString().trim();
                String tmType = typesSpinner.getSelectedItem().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(amount)){
                    edtAmount.setError("Specify Amount");
                    return;
                }

                if (tmType.equals("Select Payment Method")){
                    Toast.makeText(getActivity(),"Select Type of Payment",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tmType.equals("Select Type")){
                    Toast.makeText(getActivity(),"Select Type of Income",Toast.LENGTH_SHORT).show();
                    return;
                }

                float ouramountint = Float.parseFloat(amount);

                if (TextUtils.isEmpty(note)){
                    edtNote.setError("Description here!!!");
                    return;
                }

                String id = mIncomeData.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ouramountint,tmMethod,tmType,note,id,mDate);

                mIncomeData.child(id).setValue(data);



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

        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        final Spinner methodSpinner = myview.findViewById(R.id.payment_edit);
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.paytypes));
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSpinner.setAdapter(methodAdapter);

        final Spinner typesSpinner = myview.findViewById(R.id.type_edit);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.typesEx));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typesSpinner.setAdapter(typeAdapter);

        final EditText edtAmount = myview.findViewById(R.id.amount_edit);
        final EditText edtNote= myview.findViewById(R.id.note_edit);

        Button btnSave = myview.findViewById(R.id.btn_add);
        Button btnCancel = myview.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmAmount = edtAmount.getText().toString().trim();
                String tmMethod = methodSpinner.getSelectedItem().toString().trim();
                String tmType = typesSpinner.getSelectedItem().toString().trim();
                String tmNote = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmount)){
                    edtAmount.setError("Specify Amount");
                    return;
                }

                float inamount = Float.parseFloat(tmAmount);

                if (tmType.equals("Select Payment Method")){
                    Toast.makeText(getActivity(),"Select Type of Payment",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tmType.equals("Select Type")){
                    Toast.makeText(getActivity(),"Select Type of Expense",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(tmNote)){
                    edtNote.setError("Description here!!!");
                    return;
                }

                String id = mExpenseData.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(inamount,tmMethod, tmType, tmNote, id, mDate);
                mExpenseData.child(id).setValue(data);

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

    public void debtDataInsert(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        View myview = inflator.inflate(R.layout.custom_layout_for_insertdata2,null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        final Spinner methodSpinner = myview.findViewById(R.id.payment_edit);
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.paytypes));
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSpinner.setAdapter(methodAdapter);

        final EditText edtAmount = myview.findViewById(R.id.amount_edit);
        final EditText edtType = myview.findViewById(R.id.type_edit);
        final EditText edtNote= myview.findViewById(R.id.note_edit);

        Button btnSave = myview.findViewById(R.id.btn_add);
        Button btnCancel = myview.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmAmount = edtAmount.getText().toString().trim();
                String tmMethod = methodSpinner.getSelectedItem().toString().trim();
                String tmType = edtType.getText().toString().trim();
                String tmNote = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmount)){
                    edtAmount.setError("Specify Amount");
                    return;
                }

                float inamount = Float.parseFloat(tmAmount);

                if (tmType.equals("Select Payment Method")){
                    Toast.makeText(getActivity(),"Select Type of Payment",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(tmType)){
                    edtType.setError("Specify (Food,Transport,Entertainment,Tip..etc)");
                    return;
                }

                if (TextUtils.isEmpty(tmNote)){
                    edtNote.setError("Description here!!!");
                    return;
                }

                String id = mDebtData.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(inamount,tmMethod, tmType, tmNote, id, mDate);
                mDebtData.child(id).setValue(data);

                Toast.makeText(getActivity(), "Debt Data Added", Toast.LENGTH_SHORT).show();



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