package com.hasanin.hossam.ro2yacenter.AdminMenu.Students;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects.SubjectModel;
import com.hasanin.hossam.ro2yacenter.R;

import java.util.ArrayList;

public class WillStudySubjectsAdabter extends FirebaseRecyclerAdapter<SubjectModel , WillStudySubjectsAdabter.WillStudyHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public ArrayList<String> checkedSubjects;
    public Context context;
    String selectedGrade;
    public WillStudySubjectsAdabter(@NonNull FirebaseRecyclerOptions<SubjectModel> options , Context context , ArrayList<String> checkedSubjects , String selectedGrade) {
        super(options);
        this.checkedSubjects = checkedSubjects;
        this.context = context;
        this.selectedGrade = selectedGrade;
    }

    @NonNull
    @Override
    public WillStudyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.will_study_subjects_form , parent , false);
        WillStudyHolder willStudyHolder = new WillStudyHolder(layout);
        return willStudyHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final WillStudyHolder holder, int position, @NonNull final SubjectModel model) {
        if (model.getStudyGrade().contains(selectedGrade)) {
            if (!this.checkedSubjects.isEmpty()) {
                for (int i = 0; i < checkedSubjects.size(); i++) {
                    if (checkedSubjects.get(i).equals(model.getSubjectName())) {
                        holder.checkSubject.setChecked(true);
                        break;
                    }
                }
            }
            holder.subjectName.setText(model.getSubjectName());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkSubject.isChecked()) {
                        holder.checkSubject.setChecked(false);
                    } else {
                        holder.checkSubject.setChecked(true);
                    }
                }
            });
            holder.checkSubject.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkedSubjects.add(model.getSubjectName());
                    } else {
                        checkedSubjects.remove(model.getSubjectName());
                    }
                }
            });
        } else {
            holder.linearLayout.setVisibility(View.GONE);
        }
    }

    public static class WillStudyHolder extends RecyclerView.ViewHolder{

        public TextView subjectName;
        public CheckBox checkSubject;
        public LinearLayout linearLayout;
        public WillStudyHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.will_study_subject_name);
            checkSubject = (CheckBox) itemView.findViewById(R.id.check_subject);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.will_study_subject);
        }
    }
}
