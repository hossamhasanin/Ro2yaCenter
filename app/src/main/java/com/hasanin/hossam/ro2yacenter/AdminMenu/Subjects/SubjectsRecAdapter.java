package com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

/**
 * Created by mohamed on 13/07/2018.
 */

public class SubjectsRecAdapter extends FirebaseRecyclerAdapter<SubjectModel , SubjectsRecAdapter.SubjectHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ShowSubjects context;
    String selectedGrade;
    ArrayList<SubjectModel> checked;
    public SubjectsRecAdapter(@NonNull FirebaseRecyclerOptions<SubjectModel> options , ShowSubjects context , String selectedGrade) {
        super(options);
        this.context = context;
        this.selectedGrade = selectedGrade;
        this.checked = new ArrayList<>();
    }

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects , parent , false);
        SubjectHolder subjectHolder = new SubjectHolder(layout);
        return subjectHolder;
    }

    public ArrayList<Integer> selectedSubjects;

    @Override
    protected void onBindViewHolder(@NonNull final SubjectHolder holder, final int position, @NonNull final SubjectModel model) {
        if (model.getStudyGrade().contains(selectedGrade)) {
            context.supjectsListener.accept(Integer.valueOf(context.supjectsListener.getValue().toString()) + 1);
            holder.subjectName.setText(model.getSubjectName());
            holder.subjectName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checked.isEmpty()) {
                        android.support.v7.app.AlertDialog.Builder popupmess = new android.support.v7.app.AlertDialog.Builder(context);
                        View poplayout = LayoutInflater.from(context).inflate(R.layout.subject_info, null);
                        popupmess.setView(poplayout);
                        final android.support.v7.app.AlertDialog ad = popupmess.show();
                        TextView subjectTeacher = (TextView) poplayout.findViewById(R.id.show_teachername);
                        final TextView subjectDays = (TextView) poplayout.findViewById(R.id.show_days);
                        TextView subjectMoney = (TextView) poplayout.findViewById(R.id.show_money);
                        TextView subjectTime = (TextView) poplayout.findViewById(R.id.show_time);
                        TextView studentGrade = (TextView) poplayout.findViewById(R.id.sub_show_grade_status);
                        Button editSubject = (Button) poplayout.findViewById(R.id.edit_subject);
                        Button deleteSubject = (Button) poplayout.findViewById(R.id.delete_subject);

                        studentGrade.setText(model.getStudyGrade());
                        subjectTeacher.setText(model.getSubjectTeacher());
                        subjectDays.setText(TextUtils.join(" , ", model.getSubjectDays()));
                        subjectMoney.setText(model.getSubjecMoney());
                        subjectTime.setText(model.getSubjectTime());
                        editSubject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.dismiss();
                                Intent intent = new Intent(context, AddSubject.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("editMode", true);
                                bundle.putString("subjectId", model.getSubjectId());
                                bundle.putString("subjectName", model.getSubjectName());
                                bundle.putString("teacherName", model.getSubjectTeacher());
                                bundle.putString("money", model.getSubjecMoney());
                                bundle.putString("time", model.getSubjectTime());
                                bundle.putString("gradeStatus", model.getStudyGrade());
                                bundle.putStringArrayList("checkedDays", model.getSubjectDays());
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });
                        deleteSubject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.dismiss();
                                AlertDialog.Builder al = AlertMessage(context, "هل انت متأكد تريد حذف تلك المادة ؟", "حذف المادة ؟", R.drawable.ic_delete);
                                al.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                        final ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("جاري المسح ...");
                                        progressDialog.show();
                                        reference.child("subjects").child(model.getSubjectId()).removeValue();
                                        reference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    ArrayList<String> subjects = snapshot.getValue(StudentModel.class).getSubjects();
                                                    if (subjects != null && !subjects.isEmpty()) {
                                                        subjects.remove(model.getSubjectName());
                                                        reference.child("members").child(snapshot.getKey()).child("subjects").removeValue();
                                                        reference.child("members").child(snapshot.getKey()).child("subjects").setValue(subjects);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        reference.child("attendance").child(model.getSubjectId()).removeValue();
                                        reference.child("monthly").child(model.getSubjectId()).removeValue();
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
                            holder.subjectName.setBackgroundColor(ContextCompat.getColor(context , R.color.colorPrimaryDark));
                            holder.subjectName.setTextColor(Color.WHITE);
                        } else {
                            checked.remove(model);
                            holder.subjectName.setBackground(ContextCompat.getDrawable(context , R.drawable.editext_radious_shabe));
                            holder.subjectName.setTextColor(Color.BLACK);
                            if (checked.isEmpty()){
                                context.deleteAll.setVisibility(View.GONE);
                                context.close.setVisibility(View.GONE);
                                context.addMoreSubjects.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            });
            holder.subjectName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!checked.contains(model)){
                        checked.add(model);
                        holder.subjectName.setBackgroundColor(ContextCompat.getColor(context , R.color.colorPrimaryDark));
                        holder.subjectName.setTextColor(Color.WHITE);

                        context.deleteAll.setVisibility(View.VISIBLE);
                        context.close.setVisibility(View.VISIBLE);
                        context.addMoreSubjects.setVisibility(View.GONE);
                        return true;
                    }
                    return false;
                }
            });
        } else {
            holder.container.setVisibility(View.GONE);
            holder.subjectName.setVisibility(View.GONE);
        }

    }

    public AlertDialog.Builder AlertMessage(Activity context , String message , String title , int icon){
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setMessage(message).setIcon(icon).setTitle(title);
        return al;
    }

    public static class SubjectHolder extends RecyclerView.ViewHolder{

        public TextView subjectName;
        public LinearLayout container;
        public SubjectHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.subject);
            container = itemView.findViewById(R.id.subject_container);
        }
    }
}
