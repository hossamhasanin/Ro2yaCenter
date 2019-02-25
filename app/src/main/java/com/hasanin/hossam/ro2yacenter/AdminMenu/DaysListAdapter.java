package com.hasanin.hossam.ro2yacenter.AdminMenu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed on 11/07/2018.
 */

public class DaysListAdapter extends RecyclerView.Adapter<DaysListAdapter.viewHolder> {

    List<String> days;
    Context context;
    public ArrayList<String> checkedDays;
    public DaysListAdapter(ArrayList<String> days , Context context , ArrayList<String> checkedDays){
        this.days = days;
        this.context = context;
        this.checkedDays = checkedDays;
    }

    @Override
    public DaysListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_form , null);
        viewHolder view_holder = new viewHolder(layout);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        if (!checkedDays.isEmpty()) {
            for (int i=0;i<checkedDays.size();i++){
                if (checkedDays.get(i).equals(days.get(position))){
                    holder.checkDay.setChecked(true);
                    break;
                }
            }
        }
        holder.dayName.setText(days.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkDay.isChecked()) {
                    holder.checkDay.setChecked(false);
                } else {
                    holder.checkDay.setChecked(true);
                }
            }
        });
        holder.checkDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkedDays.add(days.get(position));
                } else {
                    //Toast.makeText(context , Integer.toString(checkedDays.size()) , Toast.LENGTH_LONG).show();
                    //deleteCheckedDay(checkedDays , days.get(position));
                    checkedDays.remove(days.get(position));
                    //Toast.makeText(context , Integer.toString(checkedDays.size()) , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        public TextView dayName;
        public CheckBox checkDay;
        public LinearLayout linearLayout;
        public viewHolder(View itemView) {
            super(itemView);
            dayName = (TextView) itemView.findViewById(R.id.day_name);
            checkDay = (CheckBox) itemView.findViewById(R.id.check_day);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.the_day);
        }
    }
}
