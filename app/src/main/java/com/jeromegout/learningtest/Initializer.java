package com.jeromegout.learningtest;

import android.app.Application;

import com.jeromegout.learningtest.model.Model;

public class Initializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //- Model
        Model.instance.setContext(this);
        Model.instance.loadModel();
    }
}
