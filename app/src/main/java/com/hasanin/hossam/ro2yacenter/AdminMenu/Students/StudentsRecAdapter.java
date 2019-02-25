package com.hasanin.hossam.ro2yacenter.AdminMenu.Students;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.AddSubject;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class StudentsRecAdapter extends FirebaseRecyclerAdapter<StudentModel , StudentsRecAdapter.StudentHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Activity context;
    public String searchQuery = "";
    public StudentsRecAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options , Activity context) {
        super(options);
        this.context = context;
    }

    public void setSearchQuery(String searchQuery){
        this.searchQuery = searchQuery;
    }


    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.students , parent , false);
        StudentHolder studentHolder = new StudentHolder(layout);
        return studentHolder;
    }

    DatabaseReference databaseReference;
    @Override
    protected void onBindViewHolder(@NonNull StudentHolder holder, int position, @NonNull final StudentModel model) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (model.getSubjects() == null){
            ArrayList<String> s = new ArrayList<String>();
            s.add("empty");
            model.setSubjects(s);
        }
        if (!model.isIsadmin() && model.getSubjects().get(0) != "none") {
            holder.studentName.setText(model.getName());
            holder.studentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder popupmess = new android.support.v7.app.AlertDialog.Builder(context);
                    View poplayout = LayoutInflater.from(context).inflate(R.layout.student_info, null);
                    popupmess.setView(poplayout);
                    final android.support.v7.app.AlertDialog ad = popupmess.show();
                    TextView studentName = (TextView) poplayout.findViewById(R.id.show_studentname);
                    TextView studentCode = (TextView) poplayout.findViewById(R.id.show_code);
                    final TextView studentSubjects = (TextView) poplayout.findViewById(R.id.show_subjects);
                    Button editStudent = (Button) poplayout.findViewById(R.id.edit_student);
                    Button deleteStudent = (Button) poplayout.findViewById(R.id.delete_student);

                    studentName.setText(model.getName());
                    studentCode.setText(model.getCode());
                    if(!model.getSubjects().get(0).equals("empty")) {
                        studentSubjects.setText(TextUtils.join(" , ", model.getSubjects()));
                    } else {
                        studentSubjects.setText("لا يوجد له مواد بعد !");
                    }
                    editStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ad.dismiss();
                            Intent intent = new Intent(context, AddStudent.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("editMode", true);
                            bundle.putString("studentName", model.getName());
                            bundle.putString("code", model.getCode());
                            bundle.putStringArrayList("checkedSubjects", model.getSubjects());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    deleteStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ad.dismiss();
                            AlertDialog.Builder al = AlertMessage(context, "هل انت متأكد تريد حذف ذلك الطالب ؟", "حذف الطالب ؟", R.drawable.ic_delete);
                            al.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog progressDialog = new ProgressDialog(context);
                                    progressDialog.setMessage("يتم المسح انتظر ...");
                                    progressDialog.show();
                                    DatabaseReference reference = databaseReference.child("members");
                                    reference.child(model.getCode()).removeValue();
                                    final DatabaseReference attendance = databaseReference.child("attendance");
                                    attendance.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                for (DataSnapshot s : snapshot.getChildren()){
                                                    attendance.child(snapshot.getKey()).child(s.getKey()).child("attendantStudents").child(model.getCode()).removeValue();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    final DatabaseReference monthly = databaseReference.child("monthly");
                                    monthly.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                for (DataSnapshot s : snapshot.getChildren()){
                                                    attendance.child(snapshot.getKey()).child(s.getKey()).child("usersCode").child(model.getCode()).removeValue();
                                                }
                                            }
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            al.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            al.show();
                        }
                    });
                }
            });
        } else {
            holder.studentContainer.setVisibility(View.GONE);
            holder.studentName.setVisibility(View.GONE);
        }
    }

    public AlertDialog.Builder AlertMessage(Activity context , String message , String title , int icon){
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setMessage(message).setIcon(icon).setTitle(title);
        return al;
    }

    public static class StudentHolder extends RecyclerView.ViewHolder{

        public TextView studentName;
        public LinearLayout studentContainer;
        public StudentHolder(View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.student);
            studentContainer = (LinearLayout) itemView.findViewById(R.id.student_container);
        }
    }
}
