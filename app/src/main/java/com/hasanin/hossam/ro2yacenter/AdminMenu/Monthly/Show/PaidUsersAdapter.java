package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.Show;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly.MonthlyModel;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Students.StudentModel;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class PaidUsersAdapter extends FirebaseRecyclerAdapter<StudentModel , PaidUsersAdapter.StudentHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Activity context;
    String subjectName , partDate , selectedGrade , subjectId;
    public PaidUsersAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options , Activity context , String subjectName , String partDate , String selectedGrade , String subjectId) {
        super(options);
        this.context = context;
        this.subjectName = subjectName;
        this.partDate = partDate;
        this.selectedGrade = selectedGrade;
        this.subjectId = subjectId;
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.paid_users , parent , false);
        StudentHolder studentHolder = new StudentHolder(layout);
        return studentHolder;
    }

    DatabaseReference reference;
    Boolean is_paidStatus;
    ArrayList<String> paidUsers;
    @Override
    protected void onBindViewHolder(@NonNull final StudentHolder holder, int position, @NonNull final StudentModel model) {
        if (model.getSubjects().contains(subjectName) && model.getStudyGrade().equals(selectedGrade)) {
            paidUsers = new ArrayList<String>();
            holder.studentName.setText(model.getName());
            reference = FirebaseDatabase.getInstance().getReference();
            holder.studentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder al = new AlertDialog.Builder(context);
                    View layout = LayoutInflater.from(context).inflate(R.layout.paid_users_info , null);
                    al.setView(layout);
                    AlertDialog ad = al.show();
                    TextView studentName = (TextView) layout.findViewById(R.id.show_studentname);
                    studentName.setText(model.getName());
                    TextView studentCode = (TextView) layout.findViewById(R.id.show_code);
                    studentCode.setText(model.getCode());
                    final TextView is_paid = (TextView) layout.findViewById(R.id.is_paid);
                    final Button changePaidStatus = (Button) layout.findViewById(R.id.change_paid_status);
                    reference.child("monthly").child(subjectId).child(partDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            MonthlyModel monthlyModel = dataSnapshot.getValue(MonthlyModel.class);
                            if (monthlyModel.getUsersCode() != null) {
                                if (monthlyModel.getUsersCode().contains(model.getCode())) {
                                    is_paid.setText("تم الدفع");
                                    is_paidStatus = true;
                                } else {
                                    is_paid.setText("لم يتم الدفع");
                                    is_paidStatus = false;
                                }
                            } else {
                                is_paid.setText("لم يتم الدفع");
                                is_paidStatus = false;
                            }
                            if (is_paidStatus){
                                changePaidStatus.setBackground(ContextCompat.getDrawable(context , R.drawable.button_red_flat_shape));
                                changePaidStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context , R.drawable.ic_delete) , null , null , null);
                                changePaidStatus.setText("ألغاء الدفع");
                            } else {
                                changePaidStatus.setBackground(ContextCompat.getDrawable(context , R.drawable.button_green_flat_shape));
                                changePaidStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context , R.drawable.ic_done) , null , null , null);
                                changePaidStatus.setText("أثبات الدفع");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    changePaidStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reference.child("monthly").child(subjectId).child(partDate).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    MonthlyModel monthlyModel = dataSnapshot.getValue(MonthlyModel.class);
                                    paidUsers = monthlyModel.getUsersCode();
                                    if (paidUsers == null){
                                        paidUsers = new ArrayList<>();
                                    }
                                    if (is_paidStatus){
                                        is_paidStatus = false;
                                        paidUsers.remove(model.getCode());
                                        reference.child("monthly").child(subjectId).child(partDate).child("usersCode").setValue(paidUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                is_paid.setText("لم يتم الدفع");
                                                changePaidStatus.setBackground(ContextCompat.getDrawable(context , R.drawable.button_green_flat_shape));
                                                changePaidStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context , R.drawable.ic_done) , null , null , null);
                                                changePaidStatus.setText("أثبات الدفع");
                                            }
                                        });
                                    } else {
                                        is_paidStatus = true;
                                        paidUsers.add(model.getCode());
                                        reference.child("monthly").child(subjectId).child(partDate).child("usersCode").setValue(paidUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                is_paid.setText("تم الدفع");
                                                changePaidStatus.setBackground(ContextCompat.getDrawable(context , R.drawable.button_red_flat_shape));
                                                changePaidStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context , R.drawable.ic_delete) , null , null , null);
                                                changePaidStatus.setText("ألغاء الدفع");
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            });
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
