package com.hasanin.hossam.ro2yacenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.AdminMenuActivity;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.Parents.ParentsMainMenu;

import java.net.InetAddress;
import java.util.Date;

public class LauncherActivity extends AppCompatActivity {

    ProgressBar progressBar;
    boolean connected;
    //ImageView launcherLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);


        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference users = firebaseDatabase.child("members");

        // Check if the user is online
//        connected = false;
//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                connected = snapshot.getValue(Boolean.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                System.err.println("Listener was cancelled");
//            }
//        });

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("session" , Context.MODE_PRIVATE);
        final String code = sharedPreferences.getString("code" , "none");
        //Toast.makeText(getApplicationContext() , code , Toast.LENGTH_SHORT).show();
        Date calendar = new Date();
        final long currentDate = calendar.getTime();
        connected = isInternetOn();
        if (connected){
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(code).exists() && !code.equals("none")) {
                        long lastLogin = dataSnapshot.child(code).child("last_login").getValue(Long.class);
                        long df = currentDate - lastLogin;
                        float daysBetween = (df / (1000 * 60 * 60 * 24));
                        if (daysBetween <= 4 && daysBetween >= 0) {
                            Boolean isAdmin = dataSnapshot.child(code).child("isadmin").getValue(Boolean.class);
                            StudentModel student = dataSnapshot.child(code).getValue(StudentModel.class);
                            if (isAdmin) {
                                Intent intent = new Intent(getApplicationContext(), AdminMenuActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("name", dataSnapshot.child(code).child("name").getValue(String.class));
                                bundle.putBoolean("isAdmin", isAdmin);
                                //bundle.putBoolean("rightComing", true);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else {
                                // Patents part
                                Intent intent = new Intent(LauncherActivity.this, ParentsMainMenu.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("name", student.getName());
                                bundle.putString("code", code);
                                bundle.putString("selectedGrade", student.getStudyGrade());
                                bundle.putStringArrayList("studentSubjects", student.getSubjects());
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.v("LogInActivity", "Error while calling the data");
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "لا يوجد اتصال بالانترنت", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}
