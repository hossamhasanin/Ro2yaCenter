package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Show;

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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AttendanceModel;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class MonthsAdapter extends FirebaseRecyclerAdapter<AttendanceModel , MonthsAdapter.MonthsHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Activity context;
    String selectedGrade , subjectId;
    public MonthsAdapter(@NonNull FirebaseRecyclerOptions<AttendanceModel> options , Activity context , String selectedGrade , String subjectId) {
        super(options);
        this.context = context;
        this.selectedGrade = selectedGrade;
        this.subjectId = subjectId;
    }

    @NonNull
    @Override
    public MonthsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.dates , parent , false);
        MonthsHolder monthsHolder = new MonthsHolder(layout);
        return monthsHolder;
    }
    ArrayList<String> storedMonths = new ArrayList<String>();
    @Override
    protected void onBindViewHolder(@NonNull MonthsHolder holder, int position, @NonNull final AttendanceModel model) {
        if (!storedMonths.contains(String.valueOf(model.getMonth()))){
            storedMonths.add(String.valueOf(model.getMonth()));
            holder.month.setText(String.valueOf(model.getMonth()));
            holder.month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , PaidUsers.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("subjectName" , model.getSubjectName());
                    bundle.putString("subjectId" , subjectId);
                    bundle.putString("selectedGrade" , selectedGrade);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.dateContainer.setVisibility(View.GONE);
            holder.month.setVisibility(View.GONE);
        }
    }

    public static class MonthsHolder extends RecyclerView.ViewHolder{

        public TextView month;
        public LinearLayout dateContainer;
        public MonthsHolder(View itemView) {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.date);
            dateContainer = (LinearLayout) itemView.findViewById(R.id.dates_contaiber);
        }
    }
}
