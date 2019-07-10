package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class AttendanceSubjectsAdapter extends FirebaseRecyclerAdapter<SubjectModel , AttendanceSubjectsAdapter.SubjectHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Activity context;
    public ArrayList<String> recordedSubjects;
    String selectedGrade;
    public AttendanceSubjectsAdapter(@NonNull FirebaseRecyclerOptions<SubjectModel> options , Activity context , ArrayList<String> recordedSubjects , String selectedGrade) {
        super(options);
        this.context = context;
        this.recordedSubjects = recordedSubjects;
        this.selectedGrade = selectedGrade;

    }

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects , parent , false);
        SubjectHolder subjectHolder = new SubjectHolder(layout);
        return subjectHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final SubjectHolder holder, int position, @NonNull final SubjectModel model) {
        if (recordedSubjects.contains(model.getSubjectId()) && model.getStudyGrade().contains(selectedGrade)){
            holder.subjectName.setText(model.getSubjectName());
            holder.subjectName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , ShowAttendanceDates.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("subjectName" , model.getSubjectName());
                    bundle.putString("subjectId" , model.getSubjectId());
                    bundle.putString("selectedGrade", selectedGrade);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.subjectContainer.setVisibility(View.GONE);
            holder.subjectName.setVisibility(View.GONE);
        }
    }

    public ArrayList<String> getRecordedSubjects() {
        return recordedSubjects;
    }

    public void setRecordedSubjects(ArrayList<String> recordedSubjects) {
        this.recordedSubjects = recordedSubjects;
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
