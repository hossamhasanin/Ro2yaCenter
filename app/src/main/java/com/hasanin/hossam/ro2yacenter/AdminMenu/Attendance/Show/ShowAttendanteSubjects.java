package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AvailableSubjectsAdapter;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.StoreAttendance;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAttendanteSubjects extends AppCompatActivity {

    RecyclerView subjectsList;
    TextView emptyMess;
    RelativeLayout attecndanceContainer;
    Button firstGrade , thirdGrade , secondGrade;
    String selectedGrade = "1";
    FirebaseRecyclerOptions<SubjectModel> firebaseRecyclerOptions;
    AttendanceSubjectsAdapter attendanceSubjectsAdapter;
    ArrayList<String> recordedSubjects;
    Activity context;
    BehaviorRelay supjectsListener = BehaviorRelay.createDefault(0);
    CompositeDisposable bag = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recordedSubjects = new ArrayList<String>();
        subjectsList = (RecyclerView) findViewById(R.id.subjects_list);
        emptyMess = (TextView) findViewById(R.id.empty_mess_error);
        attecndanceContainer = (RelativeLayout) findViewById(R.id.attendance_container);

        Query query = FirebaseDatabase.getInstance().getReference().child("subjects");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();
        attendanceSubjectsAdapter = new AttendanceSubjectsAdapter(firebaseRecyclerOptions , this , recordedSubjects , selectedGrade);
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

        firstGrade = findViewById(R.id.show_first_grade);
        secondGrade = findViewById(R.id.show_second_grade);
        thirdGrade = findViewById(R.id.show_third_grade);

        firstGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supjectsListener.accept(0);
                if (subjectsList.getVisibility() == View.GONE){
                    subjectsList.setVisibility(View.GONE);
                }

                selectedGrade = "1";
                attendanceSubjectsAdapter.stopListening();
                attendanceSubjectsAdapter = null;
                attendanceSubjectsAdapter = new AttendanceSubjectsAdapter(firebaseRecyclerOptions , ShowAttendanteSubjects.this , recordedSubjects , selectedGrade);
                subjectsList.setLayoutManager(new LinearLayoutManager(ShowAttendanteSubjects.this));
                subjectsList.setAdapter(attendanceSubjectsAdapter);
                attendanceSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_white_rounded_shabe));
                firstGrade.setTextColor(Color.BLACK);
                secondGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });

        secondGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supjectsListener.accept(0);
                if (subjectsList.getVisibility() == View.GONE){
                    subjectsList.setVisibility(View.GONE);
                }

                selectedGrade = "2";
                attendanceSubjectsAdapter.stopListening();
                attendanceSubjectsAdapter = null;
                attendanceSubjectsAdapter = new AttendanceSubjectsAdapter(firebaseRecyclerOptions , ShowAttendanteSubjects.this , recordedSubjects , selectedGrade);
                subjectsList.setLayoutManager(new LinearLayoutManager(ShowAttendanteSubjects.this));
                subjectsList.setAdapter(attendanceSubjectsAdapter);
                attendanceSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_white_rounded_shabe));
                secondGrade.setTextColor(Color.BLACK);
                thirdGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });


        thirdGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supjectsListener.accept(0);
                if (subjectsList.getVisibility() == View.GONE){
                    subjectsList.setVisibility(View.GONE);
                }

                selectedGrade = "3";
                attendanceSubjectsAdapter.stopListening();
                attendanceSubjectsAdapter = null;
                attendanceSubjectsAdapter = new AttendanceSubjectsAdapter(firebaseRecyclerOptions , ShowAttendanteSubjects.this , recordedSubjects , selectedGrade);
                subjectsList.setLayoutManager(new LinearLayoutManager(ShowAttendanteSubjects.this));
                subjectsList.setAdapter(attendanceSubjectsAdapter);
                attendanceSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(ShowAttendanteSubjects.this , R.drawable.button_white_rounded_shabe));
                thirdGrade.setTextColor(Color.BLACK);
            }
        });

        supjectsListener.subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                bag.add(d);
            }

            @Override
            public void onNext(Object o) {
                //Log.v("StudentRelay" , o.toString());
                int exists = Integer.valueOf(o.toString());
                if (exists == 0){
                    //Log.v("StudentRelay" , "no");
                    emptyMess.setVisibility(View.VISIBLE);
                } else {
                    emptyMess.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

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
