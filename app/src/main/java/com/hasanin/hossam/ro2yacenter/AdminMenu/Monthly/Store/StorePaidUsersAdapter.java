package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Store;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store.AttendanceModel;
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
    StoreUsersMonthly context;
    String subjectName;
    ArrayList<String> selectedUsers;
    String selectedGrade;
    public StorePaidUsersAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options , StoreUsersMonthly context , String subjectName , ArrayList<String> selectedUsers , String selectedGrade) {
        super(options);
        this.context = context;
        this.subjectName = subjectName;
        this.selectedUsers = selectedUsers;
        this.selectedGrade = selectedGrade;
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
    protected void onBindViewHolder(@NonNull final UserViewHolder holder, final int position, @NonNull final StudentModel model) {
        context.studentListener.accept(model);
        if (model.getSubjects().contains(subjectName) && model.getStudyGrade().equals(selectedGrade)){
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
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("انتظر يتم حسب عدد ايام الحضور ...");
                    progressDialog.show();

                    DatabaseReference attendance = FirebaseDatabase.getInstance().getReference().child("attendance");
                    attendance.child(context.subjectId).addListenerForSingleValueEvent(new ValueEventListener() {@Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int attendedDays = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                AttendanceModel attendance = snapshot.getValue(AttendanceModel.class);
                                if (attendance.getMonth() == context.currentMonth && attendance.getAttendantStudents().contains(model.getCode())){
                                    attendedDays += 1;
                                }
                            }
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            AlertDialog.Builder popupMess = new AlertDialog.Builder(context);
                            popupMess.setMessage("عدد ايام الحضور في هذا الشهر " + attendedDays);
                            popupMess.create();
                            popupMess.show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
