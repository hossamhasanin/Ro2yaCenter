package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Store;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.MonthlyModel;
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

public class StoreUsersMonthly extends AppCompatActivity {

    Bundle bundle;
    RecyclerView availableUsersList;
    TextView emptyMess;
    RelativeLayout usersContainer;
    StorePaidUsersAdapter storePaidUsersAdapter;
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
    String partDate;
    BehaviorRelay studentListener = BehaviorRelay.create();
    CompositeDisposable bag = new CompositeDisposable();
    int notExists = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_users_monthly);
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
        subjectName = bundle.getString("subjectName");
        subjectId = bundle.getString("subjectId");
        selectedGrade = bundle.getString("selectedGrade");
        getSupportActionBar().setTitle(subjectName);

        date = new Date();
        currenttime = date.getTime();
        currentMonth = new DateTime(currenttime).getMonthOfYear();
        currentDay = new DateTime(currenttime).getDayOfWeek();
        currentYear = new DateTime(currenttime).getYear();
        currentDayOfMonth = new DateTime(currenttime).getDayOfMonth();
        partDate = String.valueOf(currentMonth)+"-"+String.valueOf(currentYear);

        usersCode = new ArrayList<String>();

        availableUsersList = (RecyclerView) findViewById(R.id.available_users);
        emptyMess = (TextView) findViewById(R.id.empty_mess_error);
        usersContainer = (RelativeLayout) findViewById(R.id.users_container);

        Query query = FirebaseDatabase.getInstance().getReference("members");
        FirebaseRecyclerOptions<StudentModel> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<StudentModel>().setQuery(query , StudentModel.class).build();
        storePaidUsersAdapter = new StorePaidUsersAdapter(firebaseRecyclerOptions , this , subjectName , usersCode , selectedGrade);
        availableUsersList.setAdapter(storePaidUsersAdapter);
        availableUsersList.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("monthly");
        reference.child(subjectId).child(partDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    usersCode = dataSnapshot.getValue(MonthlyModel.class).getUsersCode();
                    storePaidUsersAdapter.setSelectedUsers(usersCode);
                    storePaidUsersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                        if (studentModel.isIsadmin() || studentModel.getSubjects().get(0).equals("none") || !studentModel.getSubjects().contains(subjectName) || !studentModel.getStudyGrade().equals(selectedGrade)) {
                            notExists += 1;
                        }


                        Log.v("StudentRelay", "c = " + notExists);

                        if (notExists == storePaidUsersAdapter.getItemCount()) {

                            Log.v("StudentRelay", "not exists");
                            if (emptyMess.getVisibility() == View.GONE) {
                                emptyMess.setVisibility(View.VISIBLE);
                                availableUsersList.setVisibility(View.GONE);
                            }
                        } else {
                            Log.v("StudentRelay", "exists");
                            if (emptyMess.getVisibility() == View.VISIBLE) {
                                emptyMess.setVisibility(View.GONE);
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
            reference = FirebaseDatabase.getInstance().getReference().child("monthly");
            //recordedId = reference.child(subjectName).push().getKey();
            if (usersCode.size() != 0 || storePaidUsersAdapter.getSelectedUsers().size() != 0) {
                MonthlyModel monthlyModel = new MonthlyModel(subjectName, currentMonth, currentYear, storePaidUsersAdapter.getSelectedUsers());
                reference.child(subjectId).child(partDate).setValue(monthlyModel);
                Toast.makeText(getApplicationContext(), "تم تسجيل الشهريات المدفوعة", Toast.LENGTH_LONG).show();
            }  else {
                Toast.makeText(getApplicationContext() , "مفيش طلاب دلوقتي" , Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        storePaidUsersAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        storePaidUsersAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
