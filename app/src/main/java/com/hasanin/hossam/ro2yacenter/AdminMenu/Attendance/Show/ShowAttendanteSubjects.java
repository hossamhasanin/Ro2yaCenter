package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAttendanteSubjects extends AppCompatActivity {

    RecyclerView subjectsList;
    TextView emptyMess;
    RelativeLayout attecndanceContainer;
    FirebaseRecyclerOptions<SubjectModel> firebaseRecyclerOptions;
    AttendanceSubjectsAdapter attendanceSubjectsAdapter;
    ArrayList<String> recordedSubjects;
    Activity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recordedSubjects = new ArrayList<String>();
        subjectsList = (RecyclerView) findViewById(R.id.subjects_list);
        emptyMess = (TextView) findViewById(R.id.empty_mess_error);
        attecndanceContainer = (RelativeLayout) findViewById(R.id.attendance_container);

        Query query = FirebaseDatabase.getInstance().getReference().child("subjects");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();
        attendanceSubjectsAdapter = new AttendanceSubjectsAdapter(firebaseRecyclerOptions , this , recordedSubjects);
        subjectsList.setAdapter(attendanceSubjectsAdapter);
        subjectsList.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("attendance");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    recordedSubjects.add(snapshot.getKey());
                }
                if (!recordedSubjects.isEmpty()){
                    subjectsList.setVisibility(View.VISIBLE);
                    emptyMess.setVisibility(View.GONE);
                    attecndanceContainer.setBackground(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout){
            Helper.logOut(this);
        } else if (item.getItemId() == R.id.about_us){

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        attendanceSubjectsAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        attendanceSubjectsAdapter.stopListening();
        super.onStop();
    }
}
