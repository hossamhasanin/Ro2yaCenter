package com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.DaysListAdapter;
import com.hasanin.hossam.ro2yacenter.Helper;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddSubject extends AppCompatActivity {

    RecyclerView daysList;
    ArrayList<String> days;
    TextView getDays;
    ArrayList<String> checkedDays;
    Context context = this;
    Bundle bundle;

    Button subjectSave;
    EditText subjectName , money , teacherName , time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
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

        subjectSave = (Button)findViewById(R.id.subject_save);
        subjectName = (EditText)findViewById(R.id.subject_name);
        money = (EditText)findViewById(R.id.money);
        teacherName = (EditText)findViewById(R.id.teacher_name);
        time = (EditText)findViewById(R.id.time);
         if (bundle.getBoolean("editMode")){
             Toast.makeText(getApplicationContext() , bundle.getString("subjectId") , Toast.LENGTH_LONG).show();
             subjectName.setText(bundle.getString("subjectName"));
             money.setText(bundle.getString("money"));
             teacherName.setText(bundle.getString("teacherName"));
             time.setText(bundle.getString("time"));
         }

        // Check if the user is online
//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
//                    Toast.makeText(getApplicationContext() , "يوجد اتصال بالانترنت" , Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext() , "لا يوجد اتصال بالانترنت" , Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                System.err.println("Listener was cancelled");
//            }
//        });

        checkedDays = new ArrayList<String>();
        days = new ArrayList<String>();
        days.add("السبت");
        days.add("الأحد");
        days.add("الأثنين");
        days.add("الثلاثاء");
        days.add("الأربعاء");
        days.add("الخميس");
        days.add("الجمعة");

        getDays = (TextView) findViewById(R.id.days);
        getDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popupmess = new AlertDialog.Builder(context);
                View poplayout = LayoutInflater.from(context).inflate(R.layout.days_list , null);
                popupmess.setView(poplayout);
                final AlertDialog ad = popupmess.show();
                daysList = (RecyclerView) poplayout.findViewById(R.id.dayss_list);
                if (bundle.getBoolean("editMode")){
                    checkedDays = bundle.getStringArrayList("checkedDays");;
                }
                final DaysListAdapter daysListAdapter = new DaysListAdapter(days, context , checkedDays);
                daysList.setAdapter(daysListAdapter);
                daysList.setLayoutManager(new GridLayoutManager(getApplicationContext() , 2));
                TextView getOut = (TextView) poplayout.findViewById(R.id.get_out);
                getOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                });
                ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        checkedDays = daysListAdapter.checkedDays;
                    }
                });
            }
        });

        subjectSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_name = subjectName.getText().toString();
                String t_name = teacherName.getText().toString();
                String m = money.getText().toString();
                String t = time.getText().toString();

                if (fildesCheck(getApplicationContext() , s_name , t_name , m , t , checkedDays)){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("subjects");
                    String subjectId = "none";
                    if (bundle.getBoolean("editMode")){
                        subjectId = bundle.getString("subjectId");
                    } else {
                        subjectId = databaseReference.push().getKey();
                    }
                    SubjectModel subject = new SubjectModel(subjectId , s_name , t_name , checkedDays ,m , t);
                    databaseReference.child(subjectId).setValue(subject);

                    subjectName.setText("");
                    teacherName.setText("");
                    money.setText("");
                    time.setText("");
                    checkedDays.clear();

                    Intent intent = new Intent(getApplicationContext() , ShowSubjects.class);
                    startActivity(intent);

                }

            }
        });

    }

    public boolean fildesCheck(Context context , String s_name , String t_name , String m , String t , ArrayList<String> checkedDays){
        if (s_name.isEmpty()){
            Toast.makeText(context , "لا تترك اسم المادة فارغا" , Toast.LENGTH_LONG).show();
            return false;
        }  else if(t_name.isEmpty()) {
            Toast.makeText(context , "لا تترك اسم المدرس فارغا" , Toast.LENGTH_LONG).show();
            return false;
        } else if (m.isEmpty()){
            Toast.makeText(context , "لا تترك مقدار الشهرية فارغا" , Toast.LENGTH_LONG).show();
            return false;
        }else if (t.isEmpty()){
            Toast.makeText(context , "لا تترك معاد المادة فارغا" , Toast.LENGTH_LONG).show();
            return false;
        } else if(checkedDays.size() == 0){
            Toast.makeText(context , "لا تترك ايام المادة فارغة" , Toast.LENGTH_LONG).show();
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
