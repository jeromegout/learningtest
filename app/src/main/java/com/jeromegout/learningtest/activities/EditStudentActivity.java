package com.jeromegout.learningtest.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

public class EditStudentActivity extends BackActivity {

    private Student student2Edit;
    private String className;
    private TextView firstNameText;
    private TextView lastNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        //- clean the toolbartitle
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(null);

        Bundle extras = getIntent().getExtras();
        if(extras == null) return;
        final String firstName = extras.getString("studentFirsName");
        final String lastName = extras.getString("studentLastName");
        className = extras.getString("className");
        student2Edit = Model.instance.getStudent(className, firstName, lastName);
        firstNameText = findViewById(R.id.first_name_id);
        firstNameText.setText(student2Edit.getFirstName());
        firstNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Model.instance.renameStudent(student2Edit, editable.toString(), lastNameText.getText().toString());
            }
        });
        lastNameText = findViewById(R.id.last_name_id);
        lastNameText.setText(student2Edit.getLastName());
        lastNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Model.instance.renameStudent(student2Edit, firstNameText.getText().toString(), editable.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_student, menu);
        menu.findItem(R.id.delete_student);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_student:
                deleteStudent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteStudent() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(String.format(getString(R.string.delete_student_title), student2Edit.getFirstName()))
                .setIcon(R.drawable.ic_person)
                .setMessage(getString(R.string.delete_student_message))
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Model.instance.removeStudent(className, student2Edit);
                                finish();
                            }
                        })
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .show();
    }
}
