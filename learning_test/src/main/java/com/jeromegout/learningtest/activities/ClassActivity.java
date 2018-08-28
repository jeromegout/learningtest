package com.jeromegout.learningtest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jeromegout.learningtest.EmptyRecyclerView;
import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.adapters.StudentAdapter;
import com.jeromegout.learningtest.model.Klass;
import com.jeromegout.learningtest.model.Model;

public class ClassActivity extends BackActivity {

    private Klass klass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        if (getIntent().getExtras() != null) {
            klass = Model.instance.getClass(getIntent().getStringExtra("className"));
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(klass.getName());
        EmptyRecyclerView recyclerView = findViewById(R.id.student_recycler_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View emptyView = findViewById(R.id.no_student_id);
        recyclerView.setEmptyView(emptyView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new StudentAdapter(this, klass));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_class, menu);
        MenuItem item = menu.findItem(R.id.random_student);
        if(klass.getStudents().size() == 0) {
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.random_student:
                Intent intent = new Intent(this, RandomDrawActivity.class);
                intent.putExtra("className", klass.getName());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}