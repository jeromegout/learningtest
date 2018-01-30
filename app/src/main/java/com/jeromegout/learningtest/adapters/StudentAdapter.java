package com.jeromegout.learningtest.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.activities.StudentActivity;
import com.jeromegout.learningtest.model.Grade;
import com.jeromegout.learningtest.model.Klass;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

import java.util.List;

/**
 * Created by jeromegout on 10/01/2018.
 *
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.Holder> implements Model.OnStudentChangedListener {

    private final Klass klass;
    private final Context context;

    class Holder extends RecyclerView.ViewHolder {
        TextView firstName;
        TextView lastName;
        TextView score;
        ImageView lastScore1;
        ImageView lastScore2;
        ImageView lastScore3;

        Holder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.student_first_name_id);
            lastName = itemView.findViewById(R.id.student_last_name_id);
            score = itemView.findViewById(R.id.student_global_score_id);
            lastScore1 = itemView.findViewById(R.id.student_score_last1_id);
            lastScore2 = itemView.findViewById(R.id.student_score_last2_id);
            lastScore3 = itemView.findViewById(R.id.student_score_last3_id);
        }

        void bind(final Student student) {
            if(student != null) {
                firstName.setText(student.getFirstName());
                lastName.setText(student.getLastName());
                int scoreVal = student.getScore();
                if(scoreVal > 0) {
                    score.setVisibility(View.VISIBLE);
                    score.setText(String.format(Model.getCurrentLocale(context),"%d", scoreVal));
                } else {
                    score.setVisibility(View.GONE);
                }
                setLastGrades(student);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openStudent(student);
                    }
                });

            }
        }

        private void setLastGrades(Student student) {
            List<Grade> grades = student.getLastGrades();
            int nbGrades = grades.size();
            if(nbGrades == 0) {
                lastScore1.setVisibility(View.GONE);
                lastScore2.setVisibility(View.GONE);
                lastScore3.setVisibility(View.GONE);
            } else if(nbGrades == 1) {
                lastScore1.setVisibility(View.GONE);
                lastScore2.setVisibility(View.GONE);
                lastScore3.setVisibility(View.VISIBLE);
                lastScore3.setImageDrawable(grades.get(0).getDrawable(context, false));
            } else if(nbGrades == 2) {
                lastScore1.setVisibility(View.GONE);
                lastScore2.setVisibility(View.VISIBLE);
                lastScore2.setImageDrawable(grades.get(0).getDrawable(context, false));
                lastScore3.setVisibility(View.VISIBLE);
                lastScore3.setImageDrawable(grades.get(1).getDrawable(context, false));
            } else if(nbGrades == 3) {
                lastScore1.setVisibility(View.VISIBLE);
                lastScore1.setImageDrawable(grades.get(0).getDrawable(context, false));
                lastScore2.setVisibility(View.VISIBLE);
                lastScore2.setImageDrawable(grades.get(1).getDrawable(context, false));
                lastScore3.setVisibility(View.VISIBLE);
                lastScore3.setImageDrawable(grades.get(2).getDrawable(context, false));
            }
        }
    }

    public StudentAdapter(Context context, Klass klass) {
        this.klass = klass;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(klass.getStudents().get(position));
    }

    @Override
    public int getItemCount() {
        return klass.getStudents().size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Model.instance.addListener(this);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Model.instance.removeListener(this);
    }

    @Override
    public void onStudentChanged(Student student) {
        notifyItemChanged(Model.instance.getStudentPositionInHisClass(student));
    }

    private void openStudent(Student student) {
        Intent intent = new Intent(context, StudentActivity.class);
        intent.putExtra("studentFirsName", student.getFirstName());
        intent.putExtra("studentLastName", student.getLastName());
        intent.putExtra("className", klass.getName());
        context.startActivity(intent);
    }
}
