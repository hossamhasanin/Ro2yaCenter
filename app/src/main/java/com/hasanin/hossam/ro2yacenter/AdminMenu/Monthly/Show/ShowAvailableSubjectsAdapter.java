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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class ShowAvailableSubjectsAdapter extends FirebaseRecyclerAdapter<SubjectModel , ShowAvailableSubjectsAdapter.SubjectHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    ShowMonthlySubjects context;
    String selectedGrade;
    DatabaseReference reference;
    public ShowAvailableSubjectsAdapter(@NonNull FirebaseRecyclerOptions<SubjectModel> options , ShowMonthlySubjects context , String selectedGrade) {
        super(options);
        this.context = context;
        this.selectedGrade = selectedGrade;
        this.reference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_subjects , parent , false);
        SubjectHolder subjectHolder = new SubjectHolder(layout);
        return subjectHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final SubjectHolder holder, int position, @NonNull final SubjectModel model) {
        if (model.getStudyGrade().contains(selectedGrade)) {
            reference.child("monthly").child(model.getSubjectId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        holder.subjectName.setText(model.getSubjectName());
                        holder.subjectName.setVisibility(View.VISIBLE);
                        holder.subjectContainer.setVisibility(View.VISIBLE);
                        holder.subjectName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ShowMonths.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("subjectName", model.getSubjectName());
                                bundle.putString("subjectId", model.getSubjectId());
                                bundle.putString("selectedGrade", selectedGrade);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });
                    } else {
                        context.supjectListener.accept(model);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            context.supjectListener.accept(model);
            holder.subjectContainer.setVisibility(View.GONE);
            holder.subjectName.setVisibility(View.GONE);
        }
    }

    public static class SubjectHolder extends RecyclerView.ViewHolder{

        public TextView subjectName;
        public LinearLayout subjectContainer;
        public SubjectHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.subject);
            subjectContainer = (LinearLayout) itemView.findViewById(R.id.subject_container);

        }
    }
}
