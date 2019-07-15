package com.hasanin.hossam.ro2yacenter.Parents;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show.AttendanceSubjectsAdapter;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AttendanceModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectsRecAdapter;
import com.hasanin.hossam.ro2yacenter.R;

public class AttendanceAdapter extends FirebaseRecyclerAdapter<AttendanceModel , AttendanceAdapter.AttendanceHolder> {

    ParentsMainMenu activity;
    String userCode;
    String adapterStatus;
    int month;

    public AttendanceAdapter(@NonNull FirebaseRecyclerOptions<AttendanceModel> options , ParentsMainMenu activity , String userCode , String adapterStatus) {
        super(options);
        this.activity = activity;
        this.userCode = userCode;
        this.adapterStatus = adapterStatus;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    protected void onBindViewHolder(@NonNull AttendanceHolder holder, int position, @NonNull final AttendanceModel model) {
        activity.emptinessListener.accept(model);
        if (model.getAttendantStudents().contains(userCode)){
            if (adapterStatus.equals("months")){
                holder.time.setText("شهر " + String.valueOf(model.getMonth()));
                holder.status.setVisibility(View.GONE);
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AttendanceAdapter attendanceAdapter = (AttendanceAdapter) activity.containerListAdapter;
                        attendanceAdapter.adapterStatus = "days";
                        attendanceAdapter.setMonth(model.getMonth());
                        activity.c = 0;
                        attendanceAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                if (getMonth() == model.getMonth()){
                    holder.time.setText(String.valueOf(model.getDay()));
                    holder.time.setCompoundDrawables(null , null , null , null);
                    holder.status.setText("تم الحضور");
                    holder.status.setVisibility(View.VISIBLE);
                } else {
                    holder.container.setVisibility(View.GONE);
                    holder.time.setVisibility(View.GONE);
                    holder.status.setVisibility(View.GONE);
                }
            }
        } else {
            holder.container.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.status.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.parents_attendance , parent , false);
        AttendanceHolder holder = new AttendanceHolder(layout);
        return holder;
    }

    public static class AttendanceHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        TextView time;
        TextView status;

        public AttendanceHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            time = itemView.findViewById(R.id.current_time);
            status = itemView.findViewById(R.id.status);
        }
    }
}
