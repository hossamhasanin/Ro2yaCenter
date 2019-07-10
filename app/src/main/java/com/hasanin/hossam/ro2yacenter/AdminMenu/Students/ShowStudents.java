package com.hasanin.hossam.ro2yacenter.AdminMenu.Students;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.AddSubject;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectsRecAdapter;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowStudents extends AppCompatActivity {

    RecyclerView studentsList;
    StudentsRecAdapter studentsRecAdapter;
    FloatingActionButton addMoreStudents;
    FloatingActionButton deleteAll;
    FloatingActionButton close;
    Button firstGrade , thirdGrade , secondGrade;
    TextView emptyMessError;
    RelativeLayout studentListContainer;
    FirebaseRecyclerOptions<StudentModel> firebaseRecyclerOptions;
    ShowStudents activity = this;
    Query query;
    private DatabaseReference.CompletionListener mRemoveListener =
            new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    if (error == null) {
                        Log.d("Show", "Removed: " + ref);
                        // or you can use:
                        //System.out.println("Removed: " + ref);
                    } else {
                        Log.e("Show", "Remove of " + ref + " failed: " + error.getMessage());
                    }
                }
            };
    String selectedGrade = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);
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

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        query = databaseReference.child("members");
        query.keepSynced(true);
        firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<StudentModel>().setQuery(query , StudentModel.class).build();

        studentsRecAdapter = new StudentsRecAdapter(firebaseRecyclerOptions , this , selectedGrade);
          studentsList = (RecyclerView) findViewById(R.id.students_list);
        studentsList.setAdapter(studentsRecAdapter);
        studentsList.setLayoutManager(new LinearLayoutManager(this));
        //studentsRecAdapter.startListening();

        addMoreStudents = (FloatingActionButton) findViewById(R.id.add_more_students);
        addMoreStudents.setOnClickListener(new View.OnClickListener() {
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
                finish();
            }
        });

        firstGrade = findViewById(R.id.show_first_grade);
        secondGrade = findViewById(R.id.show_second_grade);
        thirdGrade = findViewById(R.id.show_third_grade);

        firstGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGrade = "1";
                studentsRecAdapter.stopListening();
                studentsRecAdapter = null;
                studentsRecAdapter = new StudentsRecAdapter(firebaseRecyclerOptions , activity , selectedGrade);
                studentsList.setLayoutManager(new LinearLayoutManager(activity));
                studentsList.setAdapter(studentsRecAdapter);
                studentsRecAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_white_rounded_shabe));
                firstGrade.setTextColor(Color.BLACK);
                secondGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });

        secondGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGrade = "2";
                studentsRecAdapter.stopListening();
                studentsRecAdapter = null;
                studentsRecAdapter = new StudentsRecAdapter(firebaseRecyclerOptions , activity , selectedGrade);
                studentsList.setLayoutManager(new LinearLayoutManager(activity));
                studentsList.setAdapter(studentsRecAdapter);
                studentsRecAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_white_rounded_shabe));
                secondGrade.setTextColor(Color.BLACK);
                thirdGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });


        thirdGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGrade = "3";
                studentsRecAdapter.stopListening();
                studentsRecAdapter = null;
                studentsRecAdapter = new StudentsRecAdapter(firebaseRecyclerOptions , activity , selectedGrade);
                studentsList.setLayoutManager(new LinearLayoutManager(activity));
                studentsList.setAdapter(studentsRecAdapter);
                studentsRecAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(activity , R.drawable.button_white_rounded_shabe));
                thirdGrade.setTextColor(Color.BLACK);
            }
        });


        deleteAll = (FloatingActionButton) findViewById(R.id.delete_all_students);
        close = (FloatingActionButton) findViewById(R.id.close);

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("يتم المسح انتظر ...");
                progressDialog.show();
                for (final StudentModel student : studentsRecAdapter.checked){
                    databaseReference.child("members").child(student.getCode()).removeValue(mRemoveListener);
                    databaseReference.child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    databaseReference.child("attendance").child(snapshot.getKey()).child(s.getKey()).child("attendantStudents").child(student.getCode()).removeValue(mRemoveListener);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    databaseReference.child("monthly").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    databaseReference.child("monthly").child(snapshot.getKey()).child(s.getKey()).child("usersCode").child(student.getCode()).removeValue(mRemoveListener);
                                }
                            }

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentsRecAdapter.checked.clear();
                studentsRecAdapter.stopListening();
                studentsRecAdapter = null;
                studentsRecAdapter = new StudentsRecAdapter(firebaseRecyclerOptions , activity , selectedGrade);
                studentsList.setLayoutManager(new LinearLayoutManager(activity));
                studentsList.setAdapter(studentsRecAdapter);
                studentsRecAdapter.startListening();

                close.setVisibility(View.GONE);
                deleteAll.setVisibility(View.GONE);
                addMoreStudents.setVisibility(View.VISIBLE);

            }
        });

        studentListContainer = (RelativeLayout) findViewById(R.id.student_list_container);
        emptyMessError = (TextView) findViewById(R.id.empty_mess_error_students);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 1){
                    studentsList.setVisibility(View.GONE);
                    studentListContainer.setBackground(ContextCompat.getDrawable(getApplicationContext() , R.drawable.library));
                    emptyMessError.setVisibility(View.VISIBLE);
                } else {
                    emptyMessError.setVisibility(View.GONE);
                    studentListContainer.setBackground(null);
                    studentsList.setVisibility(View.VISIBLE);
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
                    studentsRecAdapter.stopListening();
                    studentsRecAdapter = null;
                    Query searchQueary = query.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
                    firebaseRecyclerOptions =
                            new FirebaseRecyclerOptions.Builder<StudentModel>().setQuery(searchQueary , StudentModel.class).build();
                    studentsRecAdapter = new StudentsRecAdapter(firebaseRecyclerOptions , activity , selectedGrade);
                    studentsList.setLayoutManager(new LinearLayoutManager(activity));
                    studentsList.setAdapter(studentsRecAdapter);
                    studentsRecAdapter.startListening();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                   // Toast.makeText(getApplicationContext() , "koko" , Toast.LENGTH_LONG).show();
                    studentsRecAdapter.stopListening();
                    studentsRecAdapter = null;
                    firebaseRecyclerOptions =
                            new FirebaseRecyclerOptions.Builder<StudentModel>().setQuery(query , StudentModel.class).build();
                    studentsRecAdapter = new StudentsRecAdapter(firebaseRecyclerOptions , activity , selectedGrade);
                    studentsList.setLayoutManager(new LinearLayoutManager(activity));
                    studentsList.setAdapter(studentsRecAdapter);
                    studentsRecAdapter.startListening();
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
        studentsRecAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        studentsRecAdapter.stopListening();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
