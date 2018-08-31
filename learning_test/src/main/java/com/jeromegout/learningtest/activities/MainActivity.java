package com.jeromegout.learningtest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jeromegout.learningtest.EmptyRecyclerView;
import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.adapters.ClassCardAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmptyRecyclerView recyclerView = findViewById(R.id.classesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View emptyView = findViewById(R.id.noClassesView);
        recyclerView.setEmptyView(emptyView);
        recyclerView.setAdapter(new ClassCardAdapter(this));
    }

    //- Menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.add_class);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_class:
                Intent intent = new Intent(this, EditClassActivity.class);
                startActivity(intent);
                return true;
            case R.id.info:
                showAboutDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getString(R.string.info_action_label))
                .setIcon(null)
                .setMessage("It's up to You\n\nJérôme Gout\n\nv20180831")
                .setPositiveButton(getString(R.string.ok), null)
                .show();
        TextView messageView = dialog.findViewById(android.R.id.message);
        assert messageView != null;
        messageView.setGravity(Gravity.CENTER);
    }
}
