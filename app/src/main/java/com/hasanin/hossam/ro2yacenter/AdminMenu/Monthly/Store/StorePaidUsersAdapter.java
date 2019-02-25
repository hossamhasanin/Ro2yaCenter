package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Store;

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

public class StorePaidUsersAdapter extends FirebaseRecyclerAdapter<StudentModel , StorePaidUsersAdapter.UserViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Activity context;
    String subjectName;
    ArrayList<String> selectedUsers;
    public StorePaidUsersAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options , Activity context , String subjectName , ArrayList<String> selectedUsers) {
        super(options);
        this.context = context;
        this.subjectName = subjectName;
        this.selectedUsers = selectedUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_user , parent , false);
        UserViewHolder subjectHolder = new UserViewHolder(layout);
        return subjectHolder;
    }
    ArrayList<String> existedUsers = new ArrayList<String>();
    @Override
    protected void onBindViewHolder(@NonNull final UserViewHolder holder, int position, @NonNull final StudentModel model) {
        if (model.getSubjects().contains(subjectName)){
            existedUsers.add(model.getCode());
            if (selectedUsers == null){
                selectedUsers = new ArrayList<String>();
            }
            if (!selectedUsers.isEmpty()) {
                for (int i = 0; i < selectedUsers.size(); i++) {
                    if (selectedUsers.get(i).equals(model.getCode())) {
                        holder.selectUser.setChecked(true);
                    }
                }
            }
            holder.user.setText(model.getName());
            holder.userContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.selectUser.isChecked()){
                        holder.selectUser.setChecked(false);
                    } else {
                        holder.selectUser.setChecked(true);
                    }
                }
            });
            holder.selectUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        selectedUsers.add(model.getCode());
                    } else {
                        selectedUsers.remove(model.getCode());
                    }
                }
            });
        } else {
            holder.userContainer.setVisibility(View.GONE);
            holder.user.setVisibility(View.GONE);
            holder.selectUser.setVisibility(View.GONE);
        }
//        if (getItemCount() != 0){
//            availableUsersList.setVisibility(View.VISIBLE);
//            emptyMess.setVisibility(View.GONE);
//            usersContainer.setBackground(null);
//        }
    }

    public int getExistedUsersCount(){
        return existedUsers.size();
    }

    public ArrayList<String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(ArrayList<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
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
