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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.extrack.Model.Data;
import com.example.extrack.Model.Note;
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
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private DatabaseReference mNotesData;
    private RecyclerView recyclerView;
    private FloatingActionButton fab_note_add;

    AdapterNotes adapter;
    ArrayList<Note> list;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
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
        View myview = inflater.inflate(R.layout.fragment_note, container, false);

        mAuth=FirebaseAuth.getInstance();
        //FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();
        mNotesData= FirebaseDatabase.getInstance().getReference().child("NotesData").child(uid);
        recyclerView=myview.findViewById(R.id.recycler_id_notes);
        fab_note_add = myview.findViewById(R.id.fab_notes_btn);


        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        list=new ArrayList<>();
        adapter=new AdapterNotes(list,getContext());
        recyclerView.setAdapter(adapter);


        fab_note_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotes();
            }
        });



        mNotesData.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();





                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Note data = dataSnapshot.getValue(Note.class);
                    list.add(data);


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return myview;
    }

    private void addNotes() {

            AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View myviewm = inflater.inflate(R.layout.custom_layout_for_insertnotes,null);
            mydialog.setView(myviewm);
            final AlertDialog dialog = mydialog.create();

            dialog.setCancelable(false);

            final EditText edtTitle = myviewm.findViewById(R.id.titles_edit);
            final EditText edtNote= myviewm.findViewById(R.id.note_edit);

            Button btnSave = myviewm.findViewById(R.id.btn_add);
            Button btnCancel = myviewm.findViewById(R.id.btn_cancel);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String title = edtTitle.getText().toString().trim();
                    String note = edtNote.getText().toString().trim();

                    if (TextUtils.isEmpty(title)){
                        edtTitle.setError("Title Required");
                        return;
                    }

                    if (TextUtils.isEmpty(note)){
                        edtNote.setError("Description here!!!");
                        return;
                    }

                    String id = mNotesData.push().getKey();

                    String mDate = DateFormat.getDateInstance().format(new Date());

                    Note data = new Note(title,note,id,mDate);

                    mNotesData.child(id).setValue(data);



                    Toast.makeText(getActivity(), "Notes Data Added", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }