package com.jeromegout.learningtest.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jeromegout.learningtest.EmptyRecyclerView;
import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.adapters.GradeAdapter;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

public class StudentActivity extends BackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Bundle extras = getIntent().getExtras();
        if(extras == null) return;
        final String firstName = extras.getString("studentFirsName");
        final String lastName = extras.getString("studentLastName");
        String className = extras.getString("className");
        Student student = Model.instance.getStudent(className, firstName, lastName);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(firstName);

        TextView lastNameText = findViewById(R.id.student_last_name_id);
        lastNameText.setText(student.getLastName());
        TextView firstNameText = findViewById(R.id.student_first_name_id);
        firstNameText.setText(student.getFirstName());
        TextView score = findViewById(R.id.student_global_score_id);
        if(student.getScore() >= 0) {
            score.setText(String.format(Model.getCurrentLocale(this),"%d", student.getScore()));
        } else {
            score.setVisibility(View.GONE);
        }
        EmptyRecyclerView recyclerView = findViewById(R.id.emptyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View emptyView = findViewById(R.id.no_grade_id);
        recyclerView.setEmptyView(emptyView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new GradeAdapter(this, student));
    }
}
