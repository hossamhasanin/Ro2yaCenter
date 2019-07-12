package com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowSubjects extends AppCompatActivity {

    RecyclerView subjectsList;
    SubjectsRecAdapter subjectsRecAdapter;
    FloatingActionButton addMoreSubjects;
    FloatingActionButton deleteAll;
    FloatingActionButton close;
    TextView emptyMessError;
    RelativeLayout subjectListContainer;
    Button firstGrade , thirdGrade , secondGrade;
    FirebaseRecyclerOptions<SubjectModel> firebaseRecyclerOptions;
    Query query;
    ShowSubjects context = this;
    String selectedGrade = "1";
    DatabaseReference databaseReference;

    BehaviorRelay supjectsListener = BehaviorRelay.createDefault(0);
    CompositeDisposable bag = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_subjects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        query = databaseReference.child("subjects");
        query.keepSynced(true);
        firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();

        subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , this , selectedGrade);
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
                finish();
            }
        });

        firstGrade = findViewById(R.id.show_first_grade);
        secondGrade = findViewById(R.id.show_second_grade);
        thirdGrade = findViewById(R.id.show_third_grade);

        firstGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supjectsListener.accept(0);
                if (subjectsList.getVisibility() == View.GONE){
                    emptyMessError.setVisibility(View.GONE);
                }

                selectedGrade = "1";
                subjectsRecAdapter.stopListening();
                subjectsRecAdapter = null;
                subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , context , selectedGrade);
                subjectsList.setLayoutManager(new LinearLayoutManager(context));
                subjectsList.setAdapter(subjectsRecAdapter);
                subjectsRecAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_white_rounded_shabe));
                firstGrade.setTextColor(Color.BLACK);
                secondGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });

        secondGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supjectsListener.accept(0);
                if (subjectsList.getVisibility() == View.GONE){
                    emptyMessError.setVisibility(View.GONE);
                }

                selectedGrade = "2";
                subjectsRecAdapter.stopListening();
                subjectsRecAdapter = null;
                subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , context , selectedGrade);
                subjectsList.setLayoutManager(new LinearLayoutManager(context));
                subjectsList.setAdapter(subjectsRecAdapter);
                subjectsRecAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_white_rounded_shabe));
                secondGrade.setTextColor(Color.BLACK);
                thirdGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });


        thirdGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supjectsListener.accept(0);
                if (subjectsList.getVisibility() == View.GONE){
                    emptyMessError.setVisibility(View.GONE);
                }

                selectedGrade = "3";
                subjectsRecAdapter.stopListening();
                subjectsRecAdapter = null;
                subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , context , selectedGrade);
                subjectsList.setLayoutManager(new LinearLayoutManager(context));
                subjectsList.setAdapter(subjectsRecAdapter);
                subjectsRecAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(context , R.drawable.button_white_rounded_shabe));
                thirdGrade.setTextColor(Color.BLACK);
            }
        });

        deleteAll = (FloatingActionButton) findViewById(R.id.delete_all_subjects);
        close = (FloatingActionButton) findViewById(R.id.close);

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("يتم المسح انتظر ...");
                progressDialog.show();
                for (final SubjectModel subject : subjectsRecAdapter.checked){
                    databaseReference.child("subjects").child(subject.getSubjectId()).removeValue();
                    databaseReference.child("attendance").child(subject.getSubjectId()).removeValue();
                    databaseReference.child("monthly").child(subject.getSubjectId()).removeValue();
                    databaseReference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ArrayList<String> subjects = snapshot.getValue(StudentModel.class).getSubjects();
                                if (subjects != null && !subjects.isEmpty()) {
                                    subjects.remove(subject.getSubjectName());
                                    databaseReference.child("members").child(snapshot.getKey()).child("subjects").removeValue();
                                    databaseReference.child("members").child(snapshot.getKey()).child("subjects").setValue(subjects);
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
                subjectsRecAdapter.checked.clear();
                subjectsRecAdapter.stopListening();
                subjectsRecAdapter = null;
                subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , context , selectedGrade);
                subjectsList.setLayoutManager(new LinearLayoutManager(context));
                subjectsList.setAdapter(subjectsRecAdapter);
                subjectsRecAdapter.startListening();

                close.setVisibility(View.GONE);
                deleteAll.setVisibility(View.GONE);
                addMoreSubjects.setVisibility(View.VISIBLE);

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

        supjectsListener.subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                bag.add(d);
            }

            @Override
            public void onNext(Object o) {
                //Log.v("StudentRelay" , o.toString());
                int exists = Integer.valueOf(o.toString());
                if (exists == 0){
                    //Log.v("StudentRelay" , "no");
                    emptyMessError.setVisibility(View.VISIBLE);
                } else {
                    emptyMessError.setVisibility(View.GONE);
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
                    subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , context , selectedGrade);
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
                    subjectsRecAdapter = new SubjectsRecAdapter(firebaseRecyclerOptions , context , selectedGrade);
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
