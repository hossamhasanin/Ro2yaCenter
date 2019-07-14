package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show;

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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAttendantUsers extends AppCompatActivity {

    RecyclerView studentsList;
    FirebaseRecyclerOptions<StudentModel> firebaseRecyclerOptions;
    AttendantUsersAdapter attendantUsersAdapter;
    Bundle bundle;
    ArrayList<String> usersCode;
    String selectedGrade;
    TextView emptyMessError;
    BehaviorRelay studentListener = BehaviorRelay.create();
    CompositeDisposable bag = new CompositeDisposable();
    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendant_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyMessError = (TextView) findViewById(R.id.empty_mess_error_students);

        bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(bundle.getString("record"));
        if (bundle.getStringArrayList("usersCode").equals(null)){
            usersCode = new ArrayList<String>();
        } else {
            usersCode = bundle.getStringArrayList("usersCode");
        }
        selectedGrade = bundle.getString("selectedGrade");

        studentsList = (RecyclerView) findViewById(R.id.students_list);
        Query query = FirebaseDatabase.getInstance().getReference("members");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<StudentModel>().setQuery(query , StudentModel.class).build();
        attendantUsersAdapter = new AttendantUsersAdapter(firebaseRecyclerOptions , this , usersCode , selectedGrade);
        studentsList.setAdapter(attendantUsersAdapter);
        studentsList.setLayoutManager(new LinearLayoutManager(this));

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

                        if (c == attendantUsersAdapter.getItemCount()) {
                            Log.v("StudentRelay", "not exists");
                            if (emptyMessError.getVisibility() == View.GONE) {
                                studentsList.setVisibility(View.GONE);
                                emptyMessError.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.v("StudentRelay", "exists");
                            if (emptyMessError.getVisibility() == View.VISIBLE) {
                                emptyMessError.setVisibility(View.GONE);
                                studentsList.setVisibility(View.VISIBLE);
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
        attendantUsersAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        attendantUsersAdapter.stopListening();
        super.onStop();
    }

}
