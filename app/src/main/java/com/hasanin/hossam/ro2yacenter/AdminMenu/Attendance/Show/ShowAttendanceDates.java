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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AttendanceModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAttendanceDates extends AppCompatActivity {

    RecyclerView datesList;
    FirebaseRecyclerOptions<AttendanceModel> firebaseRecyclerOptions;
    DatesAdabter datesAdabter;
    Bundle bundle;
    String selectedGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance_dates);
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

        selectedGrade = bundle.getString("selectedGrade");

        datesList = (RecyclerView) findViewById(R.id.dates_list);
        Query query = FirebaseDatabase.getInstance().getReference().child("attendance").child(bundle.getString("subjectId"));
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<AttendanceModel>().setQuery(query , AttendanceModel.class).build();
        datesAdabter = new DatesAdabter(firebaseRecyclerOptions , this , selectedGrade);
        datesList.setAdapter(datesAdabter);
        datesList.setLayoutManager(new LinearLayoutManager(this));

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
        datesAdabter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        datesAdabter.stopListening();
        super.onStop();
    }
}
