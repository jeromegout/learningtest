package com.jeromegout.learningtest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jeromegout.learningtest.EmptyRecyclerView;
import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.adapters.GradeAdapter;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

public class StudentActivity extends BackActivity implements Model.OnStudentChangedListener {

    private Student student;
    private String className;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Bundle extras = getIntent().getExtras();
        if(extras == null) return;
        final String firstName = extras.getString("studentFirsName");
        final String lastName = extras.getString("studentLastName");
        className = extras.getString("className");
        student = Model.instance.getStudent(className, firstName, lastName);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(firstName);

        TextView lastNameText = findViewById(R.id.student_last_name_id);
        lastNameText.setText(student.getLastName());
        TextView firstNameText = findViewById(R.id.student_first_name_id);
        firstNameText.setText(student.getFirstName());
        score = findViewById(R.id.student_global_score_id);
        setStudentScore();
        EmptyRecyclerView recyclerView = findViewById(R.id.emptyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View emptyView = findViewById(R.id.no_grade_id);
        recyclerView.setEmptyView(emptyView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new GradeAdapter(this, student));
        //- listen student changes (if grades are modified)
        Model.instance.addListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //- remove useless listener
        Model.instance.removeListener(this);
    }

    private void setStudentScore() {
        if(student.getScore() >= 0) {
            score.setVisibility(View.VISIBLE);
            score.setText(String.format(Model.getCurrentLocale(this),"%d", student.getScore()));
        } else {
            score.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_grade:
                Intent intent = new Intent(this, StudentGradeActivity.class);
                intent.putExtra("studentFirsName", student.getFirstName());
                intent.putExtra("studentLastName", student.getLastName());
                intent.putExtra("className", className);
                startActivityForResult(intent, 101);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101) {
            if(resultCode == RESULT_OK){
                int rank = data.getIntExtra("gradeRank", 3); //- default rank is "very good"
                Model.instance.addStudentGrade(student, rank);
            }
        }
    }

    @Override
    public void onStudentChanged(Student student) {
        setStudentScore();
    }
}
