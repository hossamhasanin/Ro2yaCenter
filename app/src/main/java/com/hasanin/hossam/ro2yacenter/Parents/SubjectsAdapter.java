package com.hasanin.hossam.ro2yacenter.Parents;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AttendanceModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectsRecAdapter;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class SubjectsAdapter extends FirebaseRecyclerAdapter<SubjectModel , SubjectsRecAdapter.SubjectHolder> {


    ArrayList<String> studentSubjects;
    ParentsMainMenu activity;
    String studyGrade;
    String mode;

    public SubjectsAdapter(@NonNull FirebaseRecyclerOptions<SubjectModel> options , ParentsMainMenu activity , ArrayList<String> studentSubjects , String studyGrade , String mode) {
        super(options);
        this.studentSubjects = studentSubjects;
        this.activity = activity;
        this.studyGrade = studyGrade;
        this.mode = mode;
    }

    @Override
    protected void onBindViewHolder(@NonNull SubjectsRecAdapter.SubjectHolder holder, int position, @NonNull final SubjectModel model) {
        if (studentSubjects.contains(model.getSubjectName()) && model.getStudyGrade().contains(studyGrade)) {

            holder.subjectName.setText(model.getSubjectName());

            if (mode == null){
                holder.subjectName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v7.app.AlertDialog.Builder popupmess = new android.support.v7.app.AlertDialog.Builder(activity);
                        View poplayout = LayoutInflater.from(activity).inflate(R.layout.subject_info, null);
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
                        editSubject.setVisibility(View.GONE);
                        deleteSubject.setVisibility(View.GONE);
                    }
                });
            } else if (mode.equals("attendance")){

                holder.subjectName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.query = activity.databaseReference.child("attendance").child(model.getSubjectId());
                        activity.query.keepSynced(true);
                        activity.firebaseRecyclerOptions =
                                new FirebaseRecyclerOptions.Builder<AttendanceModel>().setQuery(activity.query , AttendanceModel.class).build();
                        activity.containerListAdapter.stopListening();
                        activity.containerListAdapter = new AttendanceAdapter(activity.firebaseRecyclerOptions , activity , activity.studentCode);
                        activity.containerList.setAdapter(activity.containerListAdapter);
                        activity.containerList.setLayoutManager(new LinearLayoutManager(activity));
                        activity.containerListAdapter.startListening();
                    }
                });

            } else if (mode.equals("monthly")){

            }

        } else {
            holder.container.setVisibility(View.GONE);
            holder.subjectName.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public SubjectsRecAdapter.SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects , parent , false);
        SubjectsRecAdapter.SubjectHolder subjectHolder = new SubjectsRecAdapter.SubjectHolder(layout);
        return subjectHolder;
    }
}
