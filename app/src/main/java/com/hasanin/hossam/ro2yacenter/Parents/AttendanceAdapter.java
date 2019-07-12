package com.hasanin.hossam.ro2yacenter.Parents;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show.AttendanceSubjectsAdapter;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AttendanceModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectsRecAdapter;
import com.hasanin.hossam.ro2yacenter.R;

public class AttendanceAdapter extends FirebaseRecyclerAdapter<AttendanceModel , SubjectsRecAdapter.SubjectHolder> {

    ParentsMainMenu activity;
    String userCode;

    public AttendanceAdapter(@NonNull FirebaseRecyclerOptions<AttendanceModel> options , ParentsMainMenu activity , String userCode) {
        super(options);
        this.activity = activity;
        this.userCode = userCode;
    }

    @Override
    protected void onBindViewHolder(@NonNull SubjectsRecAdapter.SubjectHolder holder, int position, @NonNull AttendanceModel model) {
        if (model.getAttendantStudents().contains(userCode)){
            String fulldate = model.getDay() + "-" + model.getMonth() + "-" + model.getYear();
            holder.subjectName.setText(fulldate);
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
