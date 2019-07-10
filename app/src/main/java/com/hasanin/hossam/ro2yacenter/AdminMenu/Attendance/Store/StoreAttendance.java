package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store;

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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectsRecAdapter;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StoreAttendance extends AppCompatActivity {

    RecyclerView availableSubjectsList;
    Button firstGrade , thirdGrade , secondGrade;
    String selectedGrade = "1";
    FirebaseRecyclerOptions<SubjectModel> firebaseRecyclerOptions;
    Query query;
    AvailableSubjectsAdapter availableSubjectsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Date date = new Date();
        long currenttime = date.getTime();
        int currentMonth = new DateTime(currenttime).getMonthOfYear();
        getSupportActionBar().setTitle("مواد شهر " + String.valueOf(currentMonth));

        availableSubjectsList = (RecyclerView) findViewById(R.id.show_available_subjects);
        query = FirebaseDatabase.getInstance().getReference().child("subjects");
        query.keepSynced(true);
        firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();

        availableSubjectsAdapter = new AvailableSubjectsAdapter(firebaseRecyclerOptions , this , selectedGrade);
        availableSubjectsList.setLayoutManager(new LinearLayoutManager(this));
        availableSubjectsList.setAdapter(availableSubjectsAdapter);

        firstGrade = findViewById(R.id.show_first_grade);
        secondGrade = findViewById(R.id.show_second_grade);
        thirdGrade = findViewById(R.id.show_third_grade);

        firstGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGrade = "1";
                availableSubjectsAdapter.stopListening();
                availableSubjectsAdapter = null;
                availableSubjectsAdapter = new AvailableSubjectsAdapter(firebaseRecyclerOptions , StoreAttendance.this , selectedGrade);
                availableSubjectsList.setLayoutManager(new LinearLayoutManager(StoreAttendance.this));
                availableSubjectsList.setAdapter(availableSubjectsAdapter);
                availableSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_white_rounded_shabe));
                firstGrade.setTextColor(Color.BLACK);
                secondGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });

        secondGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGrade = "2";
                availableSubjectsAdapter.stopListening();
                availableSubjectsAdapter = null;
                availableSubjectsAdapter = new AvailableSubjectsAdapter(firebaseRecyclerOptions , StoreAttendance.this , selectedGrade);
                availableSubjectsList.setLayoutManager(new LinearLayoutManager(StoreAttendance.this));
                availableSubjectsList.setAdapter(availableSubjectsAdapter);
                availableSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_white_rounded_shabe));
                secondGrade.setTextColor(Color.BLACK);
                thirdGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });


        thirdGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGrade = "3";
                availableSubjectsAdapter.stopListening();
                availableSubjectsAdapter = null;
                availableSubjectsAdapter = new AvailableSubjectsAdapter(firebaseRecyclerOptions , StoreAttendance.this , selectedGrade);
                availableSubjectsList.setLayoutManager(new LinearLayoutManager(StoreAttendance.this));
                availableSubjectsList.setAdapter(availableSubjectsAdapter);
                availableSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(StoreAttendance.this , R.drawable.button_white_rounded_shabe));
                thirdGrade.setTextColor(Color.BLACK);
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
    protected void onStart() {
        availableSubjectsAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        availableSubjectsAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
