package com.jeromegout.learningtest.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.jeromegout.learningtest.R;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by jeromegout on 10/01/2018.
 *
 */

public class Grade {

    private final static int VERY_BAD_DRAWABLE_ID = R.drawable.ic_very_bad;
    private final static int BAD_DRAWABLE_ID = R.drawable.ic_bad;
    private final static int GOOD_DRAWABLE_ID = R.drawable.ic_good;
    private final static int VERY_GOOD_DRAWABLE_ID = R.drawable.ic_very_good;
    private final static int VERY_BAD_COLOR_DRAWABLE_ID = R.drawable.ic_very_bad_color;
    private final static int BAD_COLOR_DRAWABLE_ID = R.drawable.ic_bad_color;
    private final static int GOOD_COLOR_DRAWABLE_ID = R.drawable.ic_good_color;
    private final static int VERY_GOOD_COLOR_DRAWABLE_ID = R.drawable.ic_very_good_color;

    private int gradeRank;
    private long date;

    Grade(int gradeRank) {
        //- unknown grade means very good ;-)
        if(gradeRank < 0 || gradeRank > 3) gradeRank = 3;
        this.gradeRank = gradeRank;
        this.date = Calendar.getInstance().getTimeInMillis();
    }

    public String getHumanReadableDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return DateFormat.getDateTimeInstance().format(cal.getTime());
    }

    long getDate() {
        return date;
    }

    public Drawable getDrawable(Context context, boolean color) {
        return getGradeDrawable(context, gradeRank, color);
    }

    public static Drawable getGradeDrawable(Context context, int grade, boolean color) {
        int drawableId;
        if (!color) {
            switch (grade) {
                case 0 : drawableId = VERY_BAD_DRAWABLE_ID; break;
                case 1 : drawableId = BAD_DRAWABLE_ID; break;
                case 2 : drawableId = GOOD_DRAWABLE_ID; break;
                case 3 : drawableId = VERY_GOOD_DRAWABLE_ID; break;
                default: drawableId = VERY_GOOD_DRAWABLE_ID; break;
            }
        } else {
            switch (grade) {
                case 0 : drawableId = VERY_BAD_COLOR_DRAWABLE_ID; break;
                case 1 : drawableId = BAD_COLOR_DRAWABLE_ID; break;
                case 2 : drawableId = GOOD_COLOR_DRAWABLE_ID; break;
                case 3 : drawableId = VERY_GOOD_COLOR_DRAWABLE_ID; break;
                default: drawableId = VERY_GOOD_COLOR_DRAWABLE_ID; break;
            }
        }
        return ContextCompat.getDrawable(context, drawableId);
    }

    int getGradeRank() {
        return gradeRank;
    }
}