package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AttendanceModel;
import com.hasanin.hossam.ro2yacenter.R;

import org.joda.time.DateTime;

public class DatesAdabter extends FirebaseRecyclerAdapter<AttendanceModel , DatesAdabter.DatesHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Activity context;
    String selectedGrade;
    public DatesAdabter(@NonNull FirebaseRecyclerOptions<AttendanceModel> options , Activity context , String selectedGrade) {
        super(options);
        this.context = context;
        this.selectedGrade = selectedGrade;
    }

    @NonNull
    @Override
    public DatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.dates , parent , false);
        DatesHolder datesHolder = new DatesHolder(layout);
        return datesHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull DatesHolder holder, int position, @NonNull final AttendanceModel model) {
        if (!model.getAttendantStudents().equals(null) && !model.getAttendantStudents().isEmpty()) {
            int currentDay = new DateTime(model.getDate()).getDayOfMonth();
            final String fullDate = currentDay + "-" + model.getMonth() + "-" + model.getYear();
            holder.date.setText(fullDate);
            holder.date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowAttendantUsers.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("usersCode", model.getAttendantStudents());
                    bundle.putString("record", fullDate);
                    bundle.putString("selectedGrade", selectedGrade);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }


    public static class DatesHolder extends RecyclerView.ViewHolder{

        public TextView date;
        public DatesHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
