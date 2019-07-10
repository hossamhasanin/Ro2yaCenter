package com.hasanin.hossam.ro2yacenter.AdminMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show.ShowAttendanteSubjects;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.StoreAttendance;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Show.ShowAvailableSubjectsAdapter;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Show.ShowMonthlySubjects;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Store.StoreMonthly;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.AddStudent;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.ShowStudents;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.AddSubject;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.ShowSubjects;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AdminMenuActivity extends AppCompatActivity {

    Button addStudent , studentsRecord , addSubject , storedSubjects , storeAttendance , attendanceRecords , addMoney , moneyRecords;
    Bundle bundle;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bundle = getIntent().getExtras();

        // Check if the user is online
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (!connected) {
                    Toast.makeText(getApplicationContext() , "لا يوجد اتصال بالانترنت" , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        addStudent = (Button) findViewById(R.id.add_student);
        addMoney = (Button) findViewById(R.id.add_money);
        addSubject = (Button) findViewById(R.id.add_subject);
        storeAttendance = (Button) findViewById(R.id.store_attendance);
        studentsRecord = (Button) findViewById(R.id.students_record);
        storedSubjects = (Button) findViewById(R.id.stored_subjects);
        attendanceRecords = (Button) findViewById(R.id.attendance_record);
        moneyRecords = (Button) findViewById(R.id.money_records);

        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , AddSubject.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("editMode" , false);
                bundle.putString("subjectId" , "");
                bundle.putString("subjectName" , "");
                bundle.putString("teacherName" , "");
                bundle.putString("money" , "");
                bundle.putString("time" , "");
                ArrayList<String> cd = new ArrayList<String>();
                bundle.putStringArrayList("checkedDays" , cd);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        storedSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , ShowSubjects.class);
                startActivity(intent);
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , AddStudent.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("editMode" , false);
                bundle.putString("studentName" , "");
                bundle.putString("code" , "");
                bundle.putStringArrayList("checkedSubjects" , new ArrayList<String>());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        studentsRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , ShowStudents.class);
                startActivity(intent);
            }
        });

        storeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , StoreAttendance.class);
                startActivity(intent);
            }
        });
        attendanceRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , ShowAttendanteSubjects.class);
                startActivity(intent);
            }
        });

        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , StoreMonthly.class);
                startActivity(intent);
            }
        });
        moneyRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , ShowMonthlySubjects.class);
                startActivity(intent);
            }
        });

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
            Helper.logOut(context);
        } else if (item.getItemId() == R.id.about_us){

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
