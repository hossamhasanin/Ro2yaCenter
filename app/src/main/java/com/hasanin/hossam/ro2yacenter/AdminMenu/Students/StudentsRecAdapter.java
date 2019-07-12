package com.hasanin.hossam.ro2yacenter.AdminMenu.Students;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
    public ShowStudents context;
    public String searchQuery = "";
    DatabaseReference databaseReference;
    DatabaseReference reference;
    DatabaseReference attendance;
    DatabaseReference monthly;
    ArrayList<StudentModel> checked;
    String selectedGrade;



    public StudentsRecAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options , ShowStudents context , String selectedGrade) {
        super(options);
        this.context = context;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.reference = databaseReference.child("members");
        this.attendance = databaseReference.child("attendance");
        this.monthly = databaseReference.child("monthly");
        this.checked = new ArrayList<StudentModel>();
        this.selectedGrade = selectedGrade;

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

    @Override
    protected void onBindViewHolder(@NonNull final StudentHolder holder, final int position, @NonNull final StudentModel model) {
        if (model.getSubjects() == null){
            ArrayList<String> s = new ArrayList<String>();
            s.add("empty");
            model.setSubjects(s);
        }
        if (!model.isIsadmin() && !model.getSubjects().get(0).equals("none") && model.getStudyGrade().equals(selectedGrade)) {
            context.studentListener.accept(Integer.valueOf(context.studentListener.getValue().toString()) + 1);
            holder.studentName.setText(model.getName());
            holder.studentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checked.isEmpty()) {
                        AlertDialog.Builder popupmess = new AlertDialog.Builder(context);
                        View poplayout = LayoutInflater.from(context).inflate(R.layout.student_info, null);
                        popupmess.setView(poplayout);
                        final AlertDialog popupmessAd = popupmess.create();
                        popupmess.show();
                        TextView studentName = (TextView) poplayout.findViewById(R.id.show_studentname);
                        TextView studentCode = (TextView) poplayout.findViewById(R.id.show_code);
                        final TextView studentSubjects = (TextView) poplayout.findViewById(R.id.show_subjects);
                        TextView studentGrade = (TextView) poplayout.findViewById(R.id.show_grade_status);
                        Button editStudent = (Button) poplayout.findViewById(R.id.edit_student);
                        Button deleteStudent = (Button) poplayout.findViewById(R.id.delete_student);

                        switch (model.getStudyGrade()) {
                            case "1":
                                studentGrade.setText("الاول الثانوي");
                                break;
                            case "2":
                                studentGrade.setText("الثاني الثانوي");
                                break;
                            case "3":
                                studentGrade.setText("الثالث الثانوي");
                                break;
                        }
                        studentName.setText(model.getName());
                        studentCode.setText(model.getCode());
                        if (!model.getSubjects().get(0).equals("empty")) {
                            studentSubjects.setText(TextUtils.join(" , ", model.getSubjects()));
                        } else {
                            studentSubjects.setText("لا يوجد له مواد بعد !");
                        }
                        editStudent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupmessAd.dismiss();
                                Intent intent = new Intent(context, AddStudent.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("editMode", true);
                                bundle.putString("studentName", model.getName());
                                bundle.putString("code", model.getCode());
                                bundle.putString("gradeStatus", model.getStudyGrade());
                                bundle.putStringArrayList("checkedSubjects", model.getSubjects());
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });
                        deleteStudent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupmessAd.dismiss();
                                final AlertDialog.Builder al = AlertMessage(context, "هل انت متأكد تريد حذف ذلك الطالب ؟", "حذف الطالب ؟", R.drawable.ic_delete);
                                final AlertDialog checkDialog = al.create();
                                al.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkDialog.dismiss();
                                        final ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("يتم المسح انتظر ...");
                                        progressDialog.show();
                                        reference.child(model.getCode()).removeValue();
                                        attendance.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    for (DataSnapshot s : snapshot.getChildren()) {
                                                        attendance.child(snapshot.getKey()).child(s.getKey()).child("attendantStudents").child(model.getCode()).removeValue();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        monthly.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    for (DataSnapshot s : snapshot.getChildren()) {
                                                        monthly.child(snapshot.getKey()).child(s.getKey()).child("usersCode").child(model.getCode()).removeValue();
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
                    } else {
                        if (!checked.contains(model)){
                            checked.add(model);
                            holder.studentName.setBackgroundColor(ContextCompat.getColor(context , R.color.colorPrimaryDark));
                            holder.studentName.setTextColor(Color.WHITE);
                        } else {
                            checked.remove(model);
                            holder.studentName.setBackground(ContextCompat.getDrawable(context , R.drawable.editext_radious_shabe));
                            holder.studentName.setTextColor(Color.BLACK);
                            if (checked.isEmpty()){
                                context.deleteAll.setVisibility(View.GONE);
                                context.close.setVisibility(View.GONE);
                                context.addMoreStudents.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
            holder.studentName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!checked.contains(model)){
                       checked.add(model);
                        holder.studentName.setBackgroundColor(ContextCompat.getColor(context , R.color.colorPrimaryDark));
                        holder.studentName.setTextColor(Color.WHITE);

                        context.deleteAll.setVisibility(View.VISIBLE);
                        context.close.setVisibility(View.VISIBLE);
                        context.addMoreStudents.setVisibility(View.GONE);
                        return true;
                    }
                    return false;
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
