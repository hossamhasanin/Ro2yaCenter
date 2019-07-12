package com.hasanin.hossam.ro2yacenter.Parents;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ParentsMainMenu extends AppCompatActivity {

    RecyclerView containerList;
    Button attendance , monthly , subjects;
    TextView name , studyGrade , code;
    String selectedGrade , studentName , studentCode;
    ArrayList<String> studentSubjects;
    Bundle bundle;
    FirebaseRecyclerAdapter containerListAdapter;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    Query query;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_main_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bundle = getIntent().getExtras();

        selectedGrade = bundle.getString("selectedGrade");
        studentName = bundle.getString("name");
        studentCode = bundle.getString("code");
        studentSubjects = bundle.getStringArrayList("studentSubjects");

        containerList = findViewById(R.id.lists_container);
        attendance = findViewById(R.id.attemdance);
        monthly = findViewById(R.id.monthly);
        subjects = findViewById(R.id.subjects);
        name = findViewById(R.id.student_name);
        studyGrade = findViewById(R.id.student_grade);
        code = findViewById(R.id.student_code);

        name.setText(studentName);
        code.setText(studentCode);
        if (selectedGrade.equals("1")){
            studyGrade.setText("الصف الاول الثانوي");
        } else if (selectedGrade.equals("2")){
            studyGrade.setText("الصف الثاني الثانوي");
        } else if (selectedGrade.equals("3")){
            studyGrade.setText("الصف الثالث الثانوي");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setNavigationIcon(R.drawable.ic_backword_white);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {onBackPressed();
                    }
                });
                subjects.setVisibility(View.GONE);
                attendance.setVisibility(View.GONE);
                monthly.setVisibility(View.GONE);
                containerList.setVisibility(View.VISIBLE);

                query = databaseReference.child("subjects");
                query.keepSynced(true);
                firebaseRecyclerOptions =
                        new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();
                containerListAdapter = new SubjectsAdapter(firebaseRecyclerOptions , ParentsMainMenu.this , studentSubjects , selectedGrade , null);
                containerList.setAdapter(containerListAdapter);
                containerList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                containerListAdapter.startListening();
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setNavigationIcon(R.drawable.ic_backword_white);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {onBackPressed();
                    }
                });
                subjects.setVisibility(View.GONE);
                attendance.setVisibility(View.GONE);
                monthly.setVisibility(View.GONE);
                containerList.setVisibility(View.VISIBLE);

                query = databaseReference.child("subjects");
                query.keepSynced(true);
                firebaseRecyclerOptions =
                        new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();
                containerListAdapter = new SubjectsAdapter(firebaseRecyclerOptions , ParentsMainMenu.this , studentSubjects , selectedGrade , "attendance");
                containerList.setAdapter(containerListAdapter);
                containerList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                containerListAdapter.startListening();
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setNavigationIcon(R.drawable.ic_backword_white);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {onBackPressed();
                    }
                });
                subjects.setVisibility(View.GONE);
                attendance.setVisibility(View.GONE);
                monthly.setVisibility(View.GONE);
                containerList.setVisibility(View.VISIBLE);

                query = databaseReference.child("subjects");
                query.keepSynced(true);
                firebaseRecyclerOptions =
                        new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();
                containerListAdapter = new SubjectsAdapter(firebaseRecyclerOptions , ParentsMainMenu.this , studentSubjects , selectedGrade , "monthly");
                containerList.setAdapter(containerListAdapter);
                containerList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                containerListAdapter.startListening();
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (containerListAdapter != null){
            containerListAdapter.stopListening();
            containerListAdapter = null;
        }
    }


    @Override
    public void onBackPressed() {
        if (containerListAdapter != null){
            containerListAdapter.stopListening();
            containerListAdapter = null;

            toolbar.setNavigationIcon(null);
            containerList.setVisibility(View.GONE);
            subjects.setVisibility(View.VISIBLE);
            attendance.setVisibility(View.VISIBLE);
            monthly.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout){
            Helper.logOut(ParentsMainMenu.this);
        } else if (item.getItemId() == R.id.about_us){

        }

        return super.onOptionsItemSelected(item);
    }

}
