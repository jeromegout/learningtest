package com.jeromegout.learningtest.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.activities.EditStudentActivity;
import com.jeromegout.learningtest.model.Klass;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

/**
 * Created by jeromegout on 08/01/2018.
 *
 */

public class EditStudentAdapter extends RecyclerView.Adapter<EditStudentAdapter.Holder> implements Model.OnStudentChangedListener {

    private final Klass klass;

    class Holder extends RecyclerView.ViewHolder {

        TextView firstName;
        TextView lastName;
        Button modifyButton;

        Holder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.student_first_name_id);
            lastName = itemView.findViewById(R.id.student_last_name_id);
            modifyButton = itemView.findViewById(R.id.modify_button_id);
        }

        void bind(final Student student) {
            if(student != null) {
                firstName.setText(student.getFirstName());
                lastName.setText(student.getLastName());
                modifyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, EditStudentActivity.class);
                        intent.putExtra("studentFirsName", student.getFirstName());
                        intent.putExtra("studentLastName", student.getLastName());
                        intent.putExtra("className", klass.getName());
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    public EditStudentAdapter(Klass klass) {
        this.klass = klass;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_edit_class, parent, false);
        return new EditStudentAdapter.Holder(itemView);
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
    public void onStudentChanged(Student student) {
        notifyItemChanged(Model.instance.getStudentPositionInHisClass(student));
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
}
