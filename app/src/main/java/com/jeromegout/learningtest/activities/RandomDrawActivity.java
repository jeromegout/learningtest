package com.jeromegout.learningtest.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeromegout.learningtest.R;
import com.jeromegout.learningtest.model.Grade;
import com.jeromegout.learningtest.model.Klass;
import com.jeromegout.learningtest.model.Model;
import com.jeromegout.learningtest.model.Student;

/**
 * Created by jeromegout on 11/01/2018.
 *
 */

public class RandomDrawActivity extends BackActivity implements View.OnClickListener {

    private Klass klass;
    private Student winner;
    private TextView firstName;
    private TextView lastName;
    private int currentGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_draw);
        if (getIntent().getExtras() != null) {
            klass = Model.instance.getClass(getIntent().getStringExtra("className"));
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle("");
        firstName = findViewById(R.id.student_first_name_id);
        lastName = findViewById(R.id.student_last_name_id);
        ImageView veryBadButton = findViewById(R.id.very_bad_id);
        veryBadButton.setOnClickListener(this);
        ImageView badButton = findViewById(R.id.bad_id);
        badButton.setOnClickListener(this);
        ImageView goodButton = findViewById(R.id.good_id);
        goodButton.setOnClickListener(this);
        ImageView veryGoodButton = findViewById(R.id.very_good_id);
        veryGoodButton.setOnClickListener(this);
        randomDraw();
    }

    private void randomDraw() {
        currentGrade = -1;
        winner = klass.randomDraw();
        if(winner != null) {
            firstName.setText(winner.getFirstName());
            lastName.setText(winner.getLastName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_randow_draw, menu);
        menu.findItem(R.id.random_again);
        MenuItem done = menu.findItem(R.id.done);
        done.setVisible(currentGrade >= 0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.random_again:
                if(currentGrade >= 0) findGradeIcon(currentGrade).setImageDrawable(Grade.getGradeDrawable(this, currentGrade, false));
                invalidateOptionsMenu();
                randomDraw();
                return true;
            case R.id.done:
                Model.instance.addStudentGrade(winner, currentGrade);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int newGrade;
        switch (view.getId()) {
            case R.id.very_bad_id : newGrade = 0; break;
            case R.id.bad_id : newGrade = 1; break;
            case R.id.good_id : newGrade = 2; break;
            case R.id.very_good_id : newGrade = 3; break;
            default: newGrade = 3; break;
        }
        updateGrades(newGrade);
    }

    private void updateGrades(int newGrade) {
        //- restore color of old grade icon (if there is one)
        if(currentGrade >= 0) findGradeIcon(currentGrade).setImageDrawable(Grade.getGradeDrawable(this, currentGrade, false));
        //- set new current grade and paint its icon in color
        currentGrade = newGrade;
        findGradeIcon(newGrade).setImageDrawable(Grade.getGradeDrawable(this, newGrade, true));
        invalidateOptionsMenu();
    }

    private ImageView findGradeIcon(int grade) {
        switch (grade) {
            case 0 : return findViewById(R.id.very_bad_id);
            case 1 : return findViewById(R.id.bad_id);
            case 2 : return findViewById(R.id.good_id);
            case 3 : return findViewById(R.id.very_good_id);
            default: return findViewById(R.id.very_good_id);
        }
    }
}
