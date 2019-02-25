package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAttendantUsers extends AppCompatActivity {

    RecyclerView studentsList;
    FirebaseRecyclerOptions<StudentModel> firebaseRecyclerOptions;
    AttendantUsersAdapter attendantUsersAdapter;
    Bundle bundle;
    ArrayList<String> usersCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendant_users);
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
        getSupportActionBar().setTitle(bundle.getString("record"));
        if (bundle.getStringArrayList("usersCode").equals(null)){
            usersCode = new ArrayList<String>();
        } else {
            usersCode = bundle.getStringArrayList("usersCode");
        }

        studentsList = (RecyclerView) findViewById(R.id.students_list);
        Query query = FirebaseDatabase.getInstance().getReference("members");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<StudentModel>().setQuery(query , StudentModel.class).build();
        attendantUsersAdapter = new AttendantUsersAdapter(firebaseRecyclerOptions , this , usersCode);
        studentsList.setAdapter(attendantUsersAdapter);
        studentsList.setLayoutManager(new LinearLayoutManager(this));
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
