package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Show;

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
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AttendanceModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowMonths extends AppCompatActivity {

    RecyclerView monthsList;
    FirebaseRecyclerOptions<AttendanceModel> firebaseRecyclerOptions;
    MonthsAdapter monthsAdapter;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_months);
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

        monthsList = (RecyclerView) findViewById(R.id.months_list);
        Query query = FirebaseDatabase.getInstance().getReference().child("attendance").child(bundle.getString("subjectId"));
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<AttendanceModel>().setQuery(query , AttendanceModel.class).build();
        monthsAdapter = new MonthsAdapter(firebaseRecyclerOptions , this , bundle.getString("selectedGrade") , bundle.getString("subjectId"));
        monthsList.setAdapter(monthsAdapter);
        monthsList.setLayoutManager(new LinearLayoutManager(this));
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
        monthsAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        monthsAdapter.stopListening();
        super.onStop();
    }
}
