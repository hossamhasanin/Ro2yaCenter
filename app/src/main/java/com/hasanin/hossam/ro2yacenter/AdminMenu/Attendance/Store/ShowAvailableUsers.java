package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store;

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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AvailableUsersAdapter;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAvailableUsers extends AppCompatActivity {

    Bundle bundle;
    RecyclerView availableUsersList;
    AvailableUsersAdapter availableUsersAdapter;
    String subjectName;
    String subjectId;
    DatabaseReference reference;
    ArrayList<String> usersCode;
    Date date;
    long currenttime;
    int currentMonth;
    int currentDay;
    int currentYear;
    int currentDayOfMonth;
    String fullDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_available_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bundle = getIntent().getExtras();
        subjectName = bundle.getString("subjectsName");
        subjectId = bundle.getString("subjectId");
        usersCode = bundle.getStringArrayList("usersCode");

        date = new Date();
        currenttime = date.getTime();
        currentMonth = new DateTime(currenttime).getMonthOfYear();
        currentDay = new DateTime(currenttime).getDayOfWeek();
        currentYear = new DateTime(currenttime).getYear();
        currentDayOfMonth = new DateTime(currenttime).getDayOfMonth();
        fullDate = String.valueOf(currentDayOfMonth) + "-" + String.valueOf(currentMonth)+"-"+String.valueOf(currentYear);

        Query query = FirebaseDatabase.getInstance().getReference("members");
        FirebaseRecyclerOptions<StudentModel> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<StudentModel>().setQuery(query , StudentModel.class).build();
        availableUsersAdapter = new AvailableUsersAdapter(firebaseRecyclerOptions , this , subjectName , usersCode);
        availableUsersList = (RecyclerView) findViewById(R.id.available_users);
        availableUsersList.setAdapter(availableUsersAdapter);
        availableUsersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_successfuly , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_done){
            reference = FirebaseDatabase.getInstance().getReference().child("attendance");
            //recordedId = reference.child(subjectName).push().getKey();
            AttendanceModel attendanceModel = new AttendanceModel(subjectName , currentMonth , currentYear , date.getTime() , currentDay , availableUsersAdapter.getUsersCode());
            reference.child(subjectId).child(fullDate).setValue(attendanceModel);
            Toast.makeText(getApplicationContext() , "تم تسجيل الحاضريين" , Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        availableUsersAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        availableUsersAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
