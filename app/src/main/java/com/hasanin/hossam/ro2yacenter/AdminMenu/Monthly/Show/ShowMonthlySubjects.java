package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Show;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;
import com.jakewharton.rxrelay2.BehaviorRelay;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowMonthlySubjects extends AppCompatActivity {

    RecyclerView availableSubjectsList;
    FirebaseRecyclerOptions<SubjectModel> firebaseRecyclerOptions;
    Query query;
    ShowAvailableSubjectsAdapter showAvailableSubjectsAdapter;
    Button firstGrade , thirdGrade , secondGrade;
    String selectedGrade = "1";
    TextView emptyMessError;
    BehaviorRelay supjectListener = BehaviorRelay.create();
    CompositeDisposable bag = new CompositeDisposable();
    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_monthly_subjects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backword_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyMessError = (TextView) findViewById(R.id.empty_mess_error);

        availableSubjectsList = (RecyclerView) findViewById(R.id.show_available_subjects);
        query = FirebaseDatabase.getInstance().getReference().child("subjects");
        query.keepSynced(true);
        firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();

        showAvailableSubjectsAdapter = new ShowAvailableSubjectsAdapter(firebaseRecyclerOptions , this , selectedGrade);
        availableSubjectsList.setLayoutManager(new LinearLayoutManager(this));
        availableSubjectsList.setAdapter(showAvailableSubjectsAdapter);

        firstGrade = findViewById(R.id.show_first_grade);
        secondGrade = findViewById(R.id.show_second_grade);
        thirdGrade = findViewById(R.id.show_third_grade);

        firstGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = 0;

                selectedGrade = "1";
                showAvailableSubjectsAdapter.stopListening();
                showAvailableSubjectsAdapter = null;
                showAvailableSubjectsAdapter = new ShowAvailableSubjectsAdapter(firebaseRecyclerOptions , ShowMonthlySubjects.this , selectedGrade);
                availableSubjectsList.setLayoutManager(new LinearLayoutManager(ShowMonthlySubjects.this));
                availableSubjectsList.setAdapter(showAvailableSubjectsAdapter);
                showAvailableSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_white_rounded_shabe));
                firstGrade.setTextColor(Color.BLACK);
                secondGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });

        secondGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = 0;

                selectedGrade = "2";
                showAvailableSubjectsAdapter.stopListening();
                showAvailableSubjectsAdapter = null;
                showAvailableSubjectsAdapter = new ShowAvailableSubjectsAdapter(firebaseRecyclerOptions , ShowMonthlySubjects.this , selectedGrade);
                availableSubjectsList.setLayoutManager(new LinearLayoutManager(ShowMonthlySubjects.this));
                availableSubjectsList.setAdapter(showAvailableSubjectsAdapter);
                showAvailableSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_white_rounded_shabe));
                secondGrade.setTextColor(Color.BLACK);
                thirdGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_shape_blue));
                thirdGrade.setTextColor(Color.WHITE);
            }
        });


        thirdGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = 0;

                selectedGrade = "3";
                showAvailableSubjectsAdapter.stopListening();
                showAvailableSubjectsAdapter = null;
                showAvailableSubjectsAdapter = new ShowAvailableSubjectsAdapter(firebaseRecyclerOptions , ShowMonthlySubjects.this , selectedGrade);
                availableSubjectsList.setLayoutManager(new LinearLayoutManager(ShowMonthlySubjects.this));
                availableSubjectsList.setAdapter(showAvailableSubjectsAdapter);
                showAvailableSubjectsAdapter.startListening();

                firstGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_shape_blue));
                firstGrade.setTextColor(Color.WHITE);
                secondGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_shape_blue));
                secondGrade.setTextColor(Color.WHITE);
                thirdGrade.setBackground(ContextCompat.getDrawable(ShowMonthlySubjects.this , R.drawable.button_white_rounded_shabe));
                thirdGrade.setTextColor(Color.BLACK);
            }
        });

        supjectListener.subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                bag.add(d);
            }

            @Override
            public void onNext(Object o) {
                try {
                    if (o != null) {
                        SubjectModel studentModel = (SubjectModel) o;
                        if (!studentModel.getStudyGrade().equals(selectedGrade)) {
                            c += 1;
                        }

                        Log.v("StudentRelay", "c = " + c);

                        if (c == showAvailableSubjectsAdapter.getItemCount()) {
                            Log.v("StudentRelay", "not exists");
                            if (emptyMessError.getVisibility() == View.GONE) {
                                emptyMessError.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.v("StudentRelay", "exists");
                            if (emptyMessError.getVisibility() == View.VISIBLE) {
                                emptyMessError.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (NullPointerException e){
                    Log.v("StudentRelay", "Error => " + e.getMessage());
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
