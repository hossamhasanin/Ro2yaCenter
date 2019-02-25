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
import com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Store.MonthlyAvailableSubjectsAdapter;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowMonthlySubjects extends AppCompatActivity {

    RecyclerView availableSubjectsList;
    FirebaseRecyclerOptions<SubjectModel> firebaseRecyclerOptions;
    Query query;
    ShowAvailableSubjectsAdapter showAvailableSubjectsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_monthly_subjects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        availableSubjectsList = (RecyclerView) findViewById(R.id.show_available_subjects);
        query = FirebaseDatabase.getInstance().getReference().child("subjects");
        query.keepSynced(true);
        firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();

        showAvailableSubjectsAdapter = new ShowAvailableSubjectsAdapter(firebaseRecyclerOptions , this);
        availableSubjectsList.setLayoutManager(new LinearLayoutManager(this));
        availableSubjectsList.setAdapter(showAvailableSubjectsAdapter);
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
        showAvailableSubjectsAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        showAvailableSubjectsAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
