package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

public class AvailableSubjectsAdapter extends FirebaseRecyclerAdapter<SubjectModel, AvailableSubjectsAdapter.SubjectHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Activity context;
    public AvailableSubjectsAdapter(@NonNull FirebaseRecyclerOptions<SubjectModel> options , Activity context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public AvailableSubjectsAdapter.SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects , parent , false);
        AvailableSubjectsAdapter.SubjectHolder subjectHolder = new AvailableSubjectsAdapter.SubjectHolder(layout);
        return subjectHolder;
    }
    ArrayList<String> usersCode;
    Date date;
    long currenttime;
    int currentMonth;
    int currentDay;
    int currentYear;
    int currentDayOfMonth;
    String fullDate;
    DatabaseReference reference;
    @Override
    protected void onBindViewHolder(@NonNull final SubjectHolder holder, int position, @NonNull final SubjectModel model) {
        reference = FirebaseDatabase.getInstance().getReference();
        holder.subjectName.setText(model.getSubjectName());
        holder.subjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                currenttime = date.getTime();
                currentMonth = new DateTime(currenttime).getMonthOfYear();
                currentDay = new DateTime(currenttime).getDayOfWeek();
                currentYear = new DateTime(currenttime).getYear();
                currentDayOfMonth = new DateTime(currenttime).getDayOfMonth();
                fullDate = String.valueOf(currentDayOfMonth) + "-" + String.valueOf(currentMonth)+"-"+String.valueOf(currentYear);
                DatabaseReference attendantUsers = reference.child("attendance").child(model.getSubjectName());
                attendantUsers.child(fullDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            usersCode = dataSnapshot.getValue(AttendanceModel.class).getAttendantStudents();
                            if (usersCode == null) {
                                usersCode = new ArrayList<String>();
                            } else {
                                reference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            ArrayList<String> subjects = snapshot.getValue(StudentModel.class).getSubjects();
                                        if (subjects == null){
                                            subjects = new ArrayList<String>();
                                        }
                                            if (subjects.contains(model.getSubjectName())){
                                                Intent intent = new Intent(context, ShowAvailableUsers.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("subjectsName", model.getSubjectName());
                                                bundle.putStringArrayList("usersCode", usersCode);
                                                intent.putExtras(bundle);
                                                context.startActivity(intent);
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        } else {
                            //Toast.makeText(context , "go" , Toast.LENGTH_LONG).show();
                            reference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        ArrayList<String> subjects = snapshot.getValue(StudentModel.class).getSubjects();
                                        if (subjects == null){
                                            subjects = new ArrayList<String>();
                                        }
                                        if (subjects.contains(model.getSubjectName())){
                                            Intent intent = new Intent(context, ShowAvailableUsers.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("subjectsName", model.getSubjectName());
                                            bundle.putStringArrayList("usersCode", new ArrayList<String>());
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        //Toast.makeText(getApplicationContext() , usersCode.get(0) , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public static class SubjectHolder extends RecyclerView.ViewHolder{

        public TextView subjectName;
        public LinearLayout subjectContainer;
        public SubjectHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.subject);
            subjectContainer = (LinearLayout) itemView.findViewById(R.id.subject_container);

        }
    }
}