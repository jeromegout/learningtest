package com.jeromegout.learningtest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.model.Grade;
import com.jeromegout.learningtest.model.Student;

/**
 * Created by jeromegout on 12/01/2018.
 *
 */


public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.Holder> {

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
            }
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
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
}
