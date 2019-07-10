package com.hasanin.hossam.ro2yacenter.AdminMenu.Students;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.ShowSubjects;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import java.security.SecureRandom;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddStudent extends AppCompatActivity {

    EditText studentName;
    TextView studentCode , willStudySubjects;
    Button studentSave;
    ArrayList<String> checkedSubjects;
    Bundle bundle;
    WillStudySubjectsAdabter willStudySubjectsAdabter;
    Context context = this;
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    public static SecureRandom random = new SecureRandom();
    String generatedCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);
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

        studentName = (EditText) findViewById(R.id.student_name);
        studentCode = (TextView) findViewById(R.id.generated_code);
        willStudySubjects = (TextView) findViewById(R.id.studing_subjects);
        studentSave = (Button) findViewById(R.id.student_save);

        generatedCode = "";
        checkedSubjects = new ArrayList<String>();
        if (bundle.getBoolean("editMode")){
            generatedCode = bundle.getString("code");
            studentName.setText(bundle.getString("studentName"));
            checkedSubjects = bundle.getStringArrayList("checkedSubjects");
        } else {
            generatedCode = generateCode(10, NUMERIC + ALPHA);
        }

        Query query = FirebaseDatabase.getInstance().getReference().child("subjects");
        query.keepSynced(true);
        FirebaseRecyclerOptions<SubjectModel> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(query , SubjectModel.class).build();
        willStudySubjectsAdabter = new WillStudySubjectsAdabter(firebaseRecyclerOptions , context , checkedSubjects);

        willStudySubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popupmess = new AlertDialog.Builder(context);
                View poplayout = LayoutInflater.from(context).inflate(R.layout.will_study_subjects , null);
                popupmess.setView(poplayout);
                final AlertDialog ad = popupmess.show();
                RecyclerView subjectsList = (RecyclerView) poplayout.findViewById(R.id.wiil_study_subjects_list);
                subjectsList.setAdapter(willStudySubjectsAdabter);
                subjectsList.setLayoutManager(new GridLayoutManager(context , 2));
                TextView getOut = (TextView) poplayout.findViewById(R.id.studing_subjects);
                getOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                });
                ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        checkedSubjects = willStudySubjectsAdabter.checkedSubjects;
                    }
                });
            }
        });

        studentCode.setText(generatedCode);
        studentCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatedCode = generateCode(10 , NUMERIC + ALPHA);
                studentCode.setText(generatedCode);
            }
        });

        studentSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedSubjects != null && !checkedSubjects.isEmpty()) {
                    if (checkedSubjects.get(0).equals("empty")) {
                        checkedSubjects.remove("empty");
                    }
                    if (fildesCheck(context, studentName.getText().toString(), checkedSubjects)) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("members");
                        StudentModel studentModel = new StudentModel(false, checkedSubjects, studentName.getText().toString(), 0, generatedCode);
                        databaseReference.child(generatedCode).setValue(studentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    studentName.setText("");
                                    checkedSubjects.clear();

                                    Intent intent = new Intent(AddStudent.this , ShowStudents.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(context , " Error => " + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content) , "لا تترك المواد التي سيدرسها فارغة !"  , Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean fildesCheck(Context context , String s_name , ArrayList<String> checkedSubjects){
        if (s_name.isEmpty()){
            //Toast.makeText(context , "لا تترك اسم الطالب فارغا" , Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(android.R.id.content) , "لا تترك اسم الطالب فارغا !"  , Snackbar.LENGTH_LONG).show();
            return false;
        } else if(checkedSubjects.isEmpty()){
            //Toast.makeText(context , "لا تترك المواد التي سيدرسها فارغة" , Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(android.R.id.content) , "لا تترك المواد التي سيدرسها فارغة !"  , Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
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
        super.onStart();
        willStudySubjectsAdabter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        willStudySubjectsAdabter.stopListening();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static String generateCode(int len, String dic) {
        String result = "";
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

}
