package com.hasanin.hossam.ro2yacenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Helper {

    public static void logOut(Activity context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("session" , Context.MODE_PRIVATE);
        String code = sharedPreferences.getString("code" , "none");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("code" , "none");
        editor.commit();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("members");
        databaseReference.child(code).child("last_login").setValue(0);
        Toast.makeText(context , "لقد سجلت الخروج" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context , LogInActivity.class);
        context.startActivity(intent);
        context.finish();
    }

}
