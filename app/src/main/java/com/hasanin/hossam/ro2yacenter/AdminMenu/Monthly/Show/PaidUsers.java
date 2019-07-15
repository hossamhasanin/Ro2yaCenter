package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Show;

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

import org.joda.time.DateTime;

import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaidUsers extends AppCompatActivity {

    RecyclerView availableUsersList;
    FirebaseRecyclerOptions<StudentModel> firebaseRecyclerOptions;
    Query query;
    PaidUsersAdapter paidUsersAdapter;
    Bundle bundle;
    TextView emptyMessError;
    BehaviorRelay studentListener = BehaviorRelay.create();
    CompositeDisposable bag = new CompositeDisposable();
    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();

        int currentMonth = new DateTime(new Date().getTime()).getMonthOfYear();
        int currentYear = new DateTime(new Date().getTime()).getYear();
        String partDate = String.valueOf(currentMonth)+"-"+String.valueOf(currentYear);

        emptyMessError = (TextView) findViewById(R.id.empty_mess_error);

        availableUsersList = (RecyclerView) findViewById(R.id.available_users);
        query = FirebaseDatabase.getInstance().getReference().child("members");
        query.keepSynced(true);
        firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<StudentModel>().setQuery(query , StudentModel.class).build();

        paidUsersAdapter = new PaidUsersAdapter(firebaseRecyclerOptions , this , bundle.getString("subjectName") , partDate , bundle.getString("selectedGrade") , bundle.getString("subjectId"));
        availableUsersList.setLayoutManager(new LinearLayoutManager(this));
        availableUsersList.setAdapter(paidUsersAdapter);

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
                        if (studentModel.isIsadmin() || studentModel.getSubjects().get(0).equals("none") || !studentModel.getStudyGrade().equals(bundle.getString("selectedGrade"))) {
                            c += 1;
                        }

                        Log.v("StudentRelay", "c = " + c);

                        if (c == paidUsersAdapter.getItemCount()) {
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
        paidUsersAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        paidUsersAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(this , .class);
//
//    }
}
