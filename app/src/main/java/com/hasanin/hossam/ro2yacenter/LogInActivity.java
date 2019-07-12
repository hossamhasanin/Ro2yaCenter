package com.hasanin.hossam.ro2yacenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.AdminMenuActivity;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.Parents.ParentsMainMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LogInActivity extends AppCompatActivity {

    Typeface arabicCairoFont , arabicCairoFontBold;
    Button returnBack , logIn , adminButton , parentsButtom;
    TextView titleDesc;
    EditText logInCode;
    RelativeLayout codeContainer;
    LinearLayout selectionContainer;
    Animation slideIn , slideOut , fadeIn , fadeOut;
    boolean isAdmin = false;
    boolean isAppeared = false;
    DatabaseReference firebaseDatabase;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("session" , Context.MODE_PRIVATE);
        String code = sharedPreferences.getString("code" , "none");
        String name = sharedPreferences.getString("name" , "none");
        String selectedGrade = sharedPreferences.getString("selectedGrade" , "none");
        Set<String> subjectsSet = sharedPreferences.getStringSet("subjects" , new HashSet<String>());
        ArrayList subjects = new ArrayList();
        subjects.addAll(subjects);

        Boolean loginAdminCheck = sharedPreferences.getBoolean("isadmin" , false);
        if (!code.equals("none")){
            Log.v("Login" , "Here");
            if (loginAdminCheck){
                Intent intent = new Intent(context, AdminMenuActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putBoolean("rightComing", true);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else {
                Intent intent2 = new Intent(context, ParentsMainMenu.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("code", code);
                bundle.putString("selectedGrade", selectedGrade);
                bundle.putStringArrayList("studentSubjects", subjects);
                intent2.putExtras(bundle);
                startActivity(intent2);
                finish();
            }

        }

        //arabicCairoFont = Typeface.createFromAsset(getAssets() , "fonts/Cairo-Regular.ttf");
        arabicCairoFontBold = Typeface.createFromAsset(getAssets() , "fonts/Cairo-Bold.ttf");
        returnBack = (Button) findViewById(R.id.returnBack);
        logIn = (Button) findViewById(R.id.logIn);
        adminButton = (Button) findViewById(R.id.adminButton);
        parentsButtom = (Button) findViewById(R.id.parentsButton);
        titleDesc = (TextView) findViewById(R.id.titleDesc);
        logInCode = (EditText) findViewById(R.id.code);
        codeContainer = (RelativeLayout) findViewById(R.id.codeContainer);
        selectionContainer = (LinearLayout) findViewById(R.id.selectionContainer);
        slideIn = AnimationUtils.loadAnimation(this , android.R.anim.slide_in_left);
        slideOut = AnimationUtils.loadAnimation(this , android.R.anim.slide_out_right);
        fadeIn = AnimationUtils.loadAnimation(this , android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this , android.R.anim.fade_out);

        //initializeFonts(arabicCairoFont);
        titleDesc.setTypeface(arabicCairoFontBold);

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleDesc.setText("الأدارة");
                titleDesc.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable( getApplicationContext() ,R.drawable.admin) , null , null , null);
                codeContainer.setVisibility(View.VISIBLE);
                codeContainer.startAnimation(slideIn);
                selectionContainer.startAnimation(fadeOut);
                selectionContainer.setVisibility(View.GONE);
                isAdmin = true;
                isAppeared = true;
            }
        });

        parentsButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleDesc.setText("أولياء الأمور");
                titleDesc.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable( getApplicationContext() ,R.drawable.pl) , null , null , null);
                codeContainer.setVisibility(View.VISIBLE);
                codeContainer.startAnimation(slideIn);
                selectionContainer.startAnimation(fadeOut);
                selectionContainer.setVisibility(View.GONE);
                isAdmin = false;
                isAppeared = true;
            }
        });

        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeContainer.startAnimation(slideOut);
                codeContainer.setVisibility(View.GONE);
                selectionContainer.startAnimation(fadeIn);
                selectionContainer.setVisibility(View.VISIBLE);
                logInCode.setText("");
                isAdmin = false;
                isAppeared = false;
            }
        });

        // Check if the user is online
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (!connected) {
                    Toast.makeText(context , "لا يوجد اتصال بالانترنت" , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!logInCode.getText().toString().isEmpty()) {
                    SignIn(logInCode.getText().toString() , isAdmin , context);
                } else {
                    Toast.makeText(getApplicationContext() , "برجاء لا تترك الكود فارغا!" , Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void SignIn(final String code , final Boolean isAdmin , final Activity context){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("تسجيل الدخول ...");
        Date calendar = new Date();
        final long currentDate = calendar.getTime();
        //SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        progressDialog.show();
        final DatabaseReference users = firebaseDatabase.child("members");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(code).exists()){
                    if (dataSnapshot.child(code).child("last_login").getValue() != "" && dataSnapshot.child(code).child("last_login").exists()){
//                       long lastLogin = dataSnapshot.child(code).child("last_login").getValue(Long.class);
//                       long df =  currentDate - lastLogin;
//                       float daysBetween = (df / (1000*60*60*24));
                        users.child(code).child("last_login").setValue(currentDate);
                    }
                    StudentModel student = dataSnapshot.child(code).getValue(StudentModel.class);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("session" , Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("code" , code);
                    editor.putBoolean("isadmin", student.isIsadmin());
                    Set<String> subjectsSet = new HashSet<String>();
                    subjectsSet.addAll(student.getSubjects());
                    editor.putStringSet("subjects" , subjectsSet);
                    editor.putString("selectedGrade" , student.getStudyGrade());
                    editor.putString("name" , dataSnapshot.child(code).child("name").getValue(String.class));
                    if (isAdmin) {
                        if (student.isIsadmin()) {


                            editor.apply();
//                            Intent intent = new Intent(context, AdminMenuActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("name", student.getName());
//                            bundle.putBoolean("isAdmin", isAdmin);
//                            bundle.putBoolean("rightComing", true);
//                            intent.putExtras(bundle);
                            progressDialog.dismiss();
//                            context.startActivity(intent);
//                            context.finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "أنك من اولياء الأمور", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        // Parents part
                        if (!student.isIsadmin()) {
                            editor.apply();
//                            Intent intent2 = new Intent(context, ParentsMainMenu.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("name", student.getName());
//                            bundle.putString("code", code);
//                            bundle.putString("selectedGrade", student.getStudyGrade());
//                            bundle.putStringArrayList("studentSubjects", student.getSubjects());
//                            intent2.putExtras(bundle);
                            progressDialog.dismiss();
//                            context.startActivity(intent2);
//                            context.finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "ذلك كود للمدراء", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(context , "كود خاطأ , بطل  لعب" , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("LogInActivity" , "Error while calling the data");
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (isAppeared){
            codeContainer.startAnimation(slideOut);
            codeContainer.setVisibility(View.GONE);
            selectionContainer.startAnimation(fadeIn);
            selectionContainer.setVisibility(View.VISIBLE);
            logInCode.setText("");
            isAdmin = false;
            isAppeared = false;
        }else {
            super.onBackPressed();
        }
    }
}
