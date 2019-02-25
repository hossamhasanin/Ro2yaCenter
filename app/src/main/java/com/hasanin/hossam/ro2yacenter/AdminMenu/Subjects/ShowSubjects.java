package com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentsRecAdapter;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowSubjects extends AppCompatActivity {

    RecyclerView subjectsList;
    SubjectsRecAdapter subjectsRecAdapter;
    FloatingActionButton addMoreSubjects;
    TextView emptyMessError;
    RelativeLayout subjectListContainer;
    FirebaseRecyclerOptions<SubjectModel> firebaseRecyclerOptions;
    Query query;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_subjects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Check if the user is online
//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (!connected) {
//                    Toast.makeText(getApplicationContext() , "لا يوجد اتصال بالانترنت" , Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                System.err.println("Listener was cancelled");
//            }
//        });

        query = FirebaseDatabase.getInstance().getReference().child("subjects");
        query.keepSynced(true);
        firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();

        subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , this);
        subjectsList = (RecyclerView) findViewById(R.id.subjects_list);
        subjectsList.setLayoutManager(new LinearLayoutManager(this));
        subjectsList.setAdapter(subjectsRecAdapter);
        subjectsRecAdapter.startListening();

        addMoreSubjects = (FloatingActionButton) findViewById(R.id.add_more_subhjects);
        addMoreSubjects.setOnClickListener(new View.OnClickListener() {
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

        subjectListContainer = (RelativeLayout) findViewById(R.id.subject_list_container);
        emptyMessError = (TextView) findViewById(R.id.empty_mess_error);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    subjectsList.setVisibility(View.GONE);
                    subjectListContainer.setBackground(ContextCompat.getDrawable(getApplicationContext() , R.drawable.library));
                    emptyMessError.setVisibility(View.VISIBLE);
                } else {
                    emptyMessError.setVisibility(View.INVISIBLE);
                    subjectListContainer.setBackground(null);
                    subjectsList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.seach_action).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
                if (!searchText.isEmpty()){
                    subjectsRecAdapter.stopListening();
                    subjectsRecAdapter = null;
                    Query searchQueary = query.orderByChild("subjectName").startAt(searchText).endAt(searchText + "\uf8ff");
                    firebaseRecyclerOptions =
                            new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(searchQueary , SubjectModel.class).build();
                    subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , context);
                    subjectsList.setLayoutManager(new LinearLayoutManager(context));
                    subjectsList.setAdapter(subjectsRecAdapter);
                    subjectsRecAdapter.startListening();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    subjectsRecAdapter.stopListening();
                    subjectsRecAdapter = null;
                    firebaseRecyclerOptions =
                            new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();
                    subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , context);
                    subjectsList.setLayoutManager(new LinearLayoutManager(context));
                    subjectsList.setAdapter(subjectsRecAdapter);
                    subjectsRecAdapter.startListening();
                }
                return false;
            }
        });
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
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subjectsRecAdapter.stopListening();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
