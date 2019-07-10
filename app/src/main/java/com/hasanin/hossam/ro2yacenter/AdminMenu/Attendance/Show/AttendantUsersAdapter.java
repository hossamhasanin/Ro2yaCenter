package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class AttendantUsersAdapter extends FirebaseRecyclerAdapter<StudentModel , AttendantUsersAdapter.StudentHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Activity context;
    ArrayList<String> usersCode;
    String selectedGrade;
    public AttendantUsersAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options , Activity context , ArrayList<String> usersCode , String selectedGrade) {
        super(options);
        this.context = context;
        this.usersCode = usersCode;
        this.selectedGrade = selectedGrade;
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendant_student_form , parent , false);
        StudentHolder studentHolder = new StudentHolder(layout);
        return studentHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentHolder holder, int position, @NonNull StudentModel model) {
        if (usersCode.contains(model.getCode()) && model.getStudyGrade().equals(selectedGrade)){
            holder.studentName.setText(model.getName());
        } else {
            holder.studentContainer.setVisibility(View.GONE);
            holder.studentName.setVisibility(View.GONE);
        }
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
