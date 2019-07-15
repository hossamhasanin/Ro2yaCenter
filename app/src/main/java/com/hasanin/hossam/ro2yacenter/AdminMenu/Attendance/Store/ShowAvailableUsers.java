package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.R;
import com.jakewharton.rxrelay2.BehaviorRelay;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAvailableUsers extends AppCompatActivity {

    Bundle bundle;
    RecyclerView availableUsersList;
    AvailableUsersAdapter availableUsersAdapter;
    String subjectName;
    String subjectId , selectedGrade;
    DatabaseReference reference;
    ArrayList<String> usersCode;
    Date date;
    long currenttime;
    int currentMonth;
    int currentDay;
    int currentYear;
    int currentDayOfMonth;
    String fullDate;
    TextView emptyMessError;
    BehaviorRelay studentListener = BehaviorRelay.create();
    CompositeDisposable bag = new CompositeDisposable();
    int c = 0;

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
        selectedGrade = bundle.getString("selectedGrade");
        usersCode = bundle.getStringArrayList("usersCode");

        emptyMessError = (TextView) findViewById(R.id.empty_mess_error);


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
        availableUsersAdapter = new AvailableUsersAdapter(firebaseRecyclerOptions , this , subjectName , usersCode , selectedGrade);
        availableUsersList = (RecyclerView) findViewById(R.id.available_users);
        availableUsersList.setAdapter(availableUsersAdapter);
        availableUsersList.setLayoutManager(new LinearLayoutManager(this));


        studentListener.subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                bag.add(d);
            }

            @Override
            public void onNext(Object o) {
                try {
                    if (o != null) {
                        StudentModel studentModel = (StudentModel) o;
                        if (studentModel.isIsadmin() || studentModel.getSubjects().get(0).equals("none") || !studentModel.getStudyGrade().equals(selectedGrade)) {
                            c += 1;
                        }

                       Log.v("StudentRelay", "c = " + c);

                        if (c == availableUsersAdapter.getItemCount()) {
                            Log.v("StudentRelay", "not exists");
                            if (emptyMessError.getVisibility() == View.GONE) {
                                availableUsersList.setVisibility(View.GONE);
                                emptyMessError.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.v("StudentRelay", "exists");
                            if (emptyMessError.getVisibility() == View.VISIBLE) {
                                emptyMessError.setVisibility(View.GONE);
                                availableUsersList.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } catch (NullPointerException e){
                    //Log.v("StudentRelay", "Error => " + e.getMessage());
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
        getMenuInflater().inflate(R.menu.save_successfuly , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_done){
            reference = FirebaseDatabase.getInstance().getReference().child("attendance");
            //recordedId = reference.child(subjectName).push().getKey();
            if (usersCode.size() != 0 || availableUsersAdapter.usersCode.size() != 0){
                AttendanceModel attendanceModel = new AttendanceModel(subjectName , currentMonth , currentYear , date.getTime() , currentDayOfMonth , availableUsersAdapter.getUsersCode());
                reference.child(subjectId).child(fullDate).setValue(attendanceModel);
                Toast.makeText(getApplicationContext() , "تم تسجيل الحاضريين" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext() , "مفيش طلاب دلوقتي" , Toast.LENGTH_LONG).show();
            }

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
