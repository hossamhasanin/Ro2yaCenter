package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class AvailableUsersAdapter extends FirebaseRecyclerAdapter<StudentModel , AvailableUsersAdapter.UserViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Activity context;
    public ArrayList<String> usersCode = new ArrayList<String>();
    public String subject;
    public AvailableUsersAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options , Activity context , String subject , ArrayList<String> usersCode) {
        super(options);
        this.context = context;
        this.subject = subject;
        this.usersCode = usersCode;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_user , parent , false);
        AvailableUsersAdapter.UserViewHolder subjectHolder = new AvailableUsersAdapter.UserViewHolder(layout);
        return subjectHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final UserViewHolder holder, int position, @NonNull final StudentModel model) {
        if (model.getSubjects() == null){
            ArrayList<String> s = new ArrayList<String>();
            s.add("empty");
            model.setSubjects(s);
        }
        if (model.getSubjects().contains(subject)) {
            if (usersCode == null){
                usersCode = new ArrayList<>();
            }
            for (String code : usersCode){
                if (code.equals(model.getCode())){
                    holder.selectUser.setChecked(true);
                }
            }
            holder.user.setText(model.getName());
            holder.userContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.selectUser.isChecked()) {
                        holder.selectUser.setChecked(false);
                    } else {
                        holder.selectUser.setChecked(true);
                    }
                }
            });
            holder.selectUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        usersCode.add(model.getCode());
                    } else {
                        usersCode.remove(model.getCode());
                    }
                }
            });
        } else {
            holder.userContainer.setVisibility(View.GONE);
            holder.selectUser.setVisibility(View.GONE);
            holder.user.setVisibility(View.GONE);
        }

    }


    public ArrayList<String> getUsersCode(){
        return this.usersCode;
    }

    public void setUsersCode(ArrayList<String> usersCode) {
        this.usersCode = usersCode;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView user;
        public CheckBox selectUser;
        public RelativeLayout userContainer;
        public UserViewHolder(View itemView) {
            super(itemView);
            user = (TextView) itemView.findViewById(R.id.user);
            selectUser = (CheckBox) itemView.findViewById(R.id.select_user);
            userContainer = (RelativeLayout) itemView.findViewById(R.id.user_container);
        }
    }
}
