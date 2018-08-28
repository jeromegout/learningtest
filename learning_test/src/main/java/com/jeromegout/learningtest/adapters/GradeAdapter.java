package com.jeromegout.learningtest.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.activities.GradeActivity;
import com.jeromegout.learningtest.model.Grade;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

/**
 * Created by jeromegout on 12/01/2018.
 *
 */


public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.Holder> implements Model.OnStudentChangedListener {

    private final Student student;
    private final Context context;

    class Holder extends RecyclerView.ViewHolder  {
        ImageView icon;
        TextView date;

        Holder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.grade_icon_id);
            date = itemView.findViewById(R.id.grade_date_id);
        }

        void bind(final Grade grade) {
            if (grade != null) {
                icon.setImageDrawable(grade.getDrawable(context, true));
                date.setText(grade.getHumanReadableDate());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGrade(grade);
                    }
                });
            }
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(student.getGrades().get(position));
    }

    @Override
    public int getItemCount() {
        return student.getGrades().size();
    }


    public GradeAdapter(Context context, Student student) {
        this.student = student;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Model.instance.addListener(this);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Model.instance.removeListener(this);
    }

    @Override
    public void onStudentChanged(Student student) {
        notifyDataSetChanged();
    }

    private void openGrade(Grade grade){
        Intent intent = new Intent(context, GradeActivity.class);
        intent.putExtra("studentFirsName", student.getFirstName());
        intent.putExtra("studentLastName", student.getLastName());
        intent.putExtra("className", student.getClassName());
        intent.putExtra("gradeDate", grade.getDate());
        context.startActivity(intent);
    }
}
