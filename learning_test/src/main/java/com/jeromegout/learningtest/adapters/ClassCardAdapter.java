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
import com.jeromegout.learningtest.activities.ClassActivity;
import com.jeromegout.learningtest.activities.EditClassActivity;
import com.jeromegout.learningtest.model.Klass;
import com.jeromegout.learningtest.model.Model;

/**
 * Created by jeromegout on 07/01/2018.
 *
 */

public class ClassCardAdapter extends RecyclerView.Adapter<ClassCardAdapter.Holder> implements Model.OnModelChangedListener,
        Model.OnClassChangedListener {

    private final Context context;

    class Holder extends RecyclerView.ViewHolder {

        TextView className;
        TextView studentNumber;
        Button modifyButton;

        Holder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_id);
            studentNumber = itemView.findViewById(R.id.class_student_number_id);
            modifyButton = itemView.findViewById(R.id.modify_button_id);
        }

        void bind(final Klass klass) {
            if(klass != null) {
                className.setText(klass.getName());
                studentNumber.setText(String.format(Model.getCurrentLocale(context), "%d", klass.getStudents().size()));
                modifyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modifyClass(klass);
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openClass(klass);
                    }
                });
            }
        }

    }

    public ClassCardAdapter(Context context) {
        this.context = context;
    }

    private void modifyClass(Klass klass) {
        Intent intent = new Intent(context, EditClassActivity.class);
        intent.putExtra("className", klass.getName());
        context.startActivity(intent);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_card, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Klass klass = Model.instance.getClass(position);
        holder.bind(klass);
    }

    @Override
    public int getItemCount() {
        return Model.instance.getClasses().size();
    }

    private void openClass(Klass klass) {
        Intent intent = new Intent(context, ClassActivity.class);
        intent.putExtra("className", klass.getName());
        context.startActivity(intent);
    }

    @Override
    public void onModelChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void onClassChanged(Klass klass) {
        notifyItemChanged(Model.instance.indexOf(klass));
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
