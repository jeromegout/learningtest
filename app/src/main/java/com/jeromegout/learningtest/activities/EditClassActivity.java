package com.jeromegout.learningtest.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jeromegout.learningtest.EmptyRecyclerView;
import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.adapters.EditStudentAdapter;
import com.jeromegout.learningtest.model.Klass;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

public class EditClassActivity extends BackActivity implements TextWatcher, Model.OnClassChangedListener {

    private Klass class2Edit;
    private EmptyRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);
        if (getIntent().getExtras() != null) {
            class2Edit = Model.instance.getClass(getIntent().getStringExtra("className"));
        } else {
            //- new class - need to create it
            class2Edit = Model.instance.createNewClass();
        }
        EditText className = findViewById(R.id.class_name_id);
        className.addTextChangedListener(this);
        className.setText(class2Edit.getName());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(class2Edit.getName());

        recyclerView = findViewById(R.id.student_recycler_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View emptyView = findViewById(R.id.no_student_id);
        recyclerView.setEmptyView(emptyView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new EditStudentAdapter(class2Edit));

        //- listen class modification (update students list)
        Model.instance.addListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_class, menu);
        menu.findItem(R.id.delete_class);
        menu.findItem(R.id.add_student);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.add_student :
                addStudentDialog();
                return true;

            case R.id.delete_class :
                deleteClass();
                return true;

            case R.id.reset_grades :
                reset_grades();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reset_grades() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(String.format(getString(R.string.delete_grades_title), class2Edit.getName()))
                .setMessage(getString(R.string.delete_grades_desc))
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Model.instance.resetGradesOf(class2Edit);
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

    private void deleteClass() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(String.format(getString(R.string.delete_class_title), class2Edit.getName()))
                .setMessage(getString(R.string.delete_class_message))
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Model.instance.removeClass(class2Edit);
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

    private void addStudentDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_student_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText firstName = promptsView.findViewById(R.id.first_name_id);
        final EditText lastName = promptsView.findViewById(R.id.last_name_id);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setTitle(getString(R.string.add_student_title))
                .setIcon(R.drawable.ic_person)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                addStudent(firstName.getText().toString(), lastName.getText().toString());
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void addStudent(String firstName, String lastName) {
        Model.instance.addStudentTo(new Student(firstName, lastName, class2Edit.getName()));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        Model.instance.renameClass(class2Edit, editable.toString());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(class2Edit.getName());
    }

    @Override
    public void onClassChanged(Klass klass) {
        class2Edit = klass;
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
