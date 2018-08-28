package com.jeromegout.learningtest.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.model.Grade;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

public class GradeActivity extends BackActivity {

    private Student student;
    private Grade grade;
    private String className;
    private ImageView gradeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Bundle extras = getIntent().getExtras();
        if(extras == null) return;
        final String firstName = extras.getString("studentFirsName");
        final String lastName = extras.getString("studentLastName");
        className = extras.getString("className");
        student = Model.instance.getStudent(className, firstName, lastName);
        Long date = extras.getLong("gradeDate");
        grade = student.getGrade(date);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(firstName);

        TextView lastNameText = findViewById(R.id.student_last_name_id);
        lastNameText.setText(student.getLastName());
        TextView firstNameText = findViewById(R.id.student_first_name_id);
        firstNameText.setText(student.getFirstName());
        gradeIcon = findViewById(R.id.grade_icon_id);
        if(grade != null) {
            gradeIcon.setImageDrawable(grade.getDrawable(this, true));
        }
        TextView dateText = findViewById(R.id.grade_date_id);
        dateText.setText(grade.getHumanReadableDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grade, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_grade:
                deleteGrade();
                return true;
            case R.id.edit_grade:
                editGrade();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editGrade() {
        Intent intent = new Intent(this, StudentGradeActivity.class);
        intent.putExtra("studentFirsName", student.getFirstName());
        intent.putExtra("studentLastName", student.getLastName());
        intent.putExtra("className", className);
        startActivityForResult(intent, 102);
    }

    private void deleteGrade() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(getString(R.string.delete_grade_title))
                .setIcon(grade.getDrawable(this, false))
                .setMessage(getString(R.string.delete_grade_message))
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Model.instance.removeStudentGrade(student, grade.getDate());
                                finish();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 102) {
            if (resultCode == RESULT_OK) {
                int rank = data.getIntExtra("gradeRank", 3);
                Model.instance.editStudentGrade(student, grade.getDate(), rank);
                gradeIcon.setImageDrawable(grade.getDrawable(this, true));
            }
        }
    }
}
