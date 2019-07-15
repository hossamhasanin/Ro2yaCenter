package com.hasanin.hossam.ro2yacenter.Parents;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.MonthlyModel;
import com.hasanin.hossam.ro2yacenter.R;

public class MonthlyAdapter extends FirebaseRecyclerAdapter<MonthlyModel , AttendanceAdapter.AttendanceHolder> {

    ParentsMainMenu activity;
    String userCode;

    public MonthlyAdapter(@NonNull FirebaseRecyclerOptions<MonthlyModel> options , ParentsMainMenu activity , String userCode) {
        super(options);
        this.activity = activity;
        this.userCode = userCode;
    }

    @Override
    protected void onBindViewHolder(@NonNull AttendanceAdapter.AttendanceHolder holder, int position, @NonNull MonthlyModel model) {
        activity.emptinessListener.accept(model);
        if (model.getUsersCode().contains(userCode)){
            holder.time.setText(String.valueOf(model.getMonth()));
            holder.time.setCompoundDrawables(null , null , null , null);
            holder.status.setText("تم الدفع");
        } else {
            holder.time.setVisibility(View.GONE);
            holder.container.setVisibility(View.GONE);
            holder.status.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public AttendanceAdapter.AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.parents_attendance , parent , false);
        AttendanceAdapter.AttendanceHolder holder = new AttendanceAdapter.AttendanceHolder(layout);
        return holder;
    }
}
