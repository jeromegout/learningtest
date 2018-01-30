package com.jeromegout.learningtest.model;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.jeromegout.learningtest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jeromegout on 07/01/2018.
 *
 */

public class Model {

    /**
     * Singleton accessor
     */
    public final static Model instance = new Model();
    private final static String MODEL_ROOT_NAME = "model";
    private final static String MODEL_VERSION = "2.0";

    private List<Klass> classes;
    private Context context;
    private List<ModelListener> listeners;

    interface ModelListener {}

    public interface OnModelChangedListener extends ModelListener {
        void onModelChanged();
    }

    public interface OnClassChangedListener extends ModelListener {

        void onClassChanged(Klass klass);
    }
    public interface OnStudentChangedListener extends ModelListener {

        void onStudentChanged(Student student);
    }

    public static Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    public Klass getClass(String className) {
        for (Klass klass : classes) {
            if(klass.getName().equals(className)) return klass;
        }
        return null;
    }

    public Klass getClass(int position) {
        try {
            return classes.get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    public Klass createNewClass() {
        Klass klass = new Klass(context.getResources().getString(R.string.unnamed_class));
        classes.add(klass);
        notifyModelChange();
        saveModel();
        return klass;
    }

    public void removeClass(Klass klass) {
        classes.remove(klass);
        notifyModelChange();
        saveModel();
    }

    public void renameClass(Klass klass, String s) {
        klass.setName(s);
        notifyClassChange(klass);
        saveModel();
    }

    public List<Klass> getClasses() {
        return classes;
    }

    public void addStudentTo(Student student) {
        Klass klass = student.getKlass();
        klass.addStudent(student);
        notifyClassChange(klass);
        saveModel();
    }

    private void notifyStudentChange(Student student) {
        for (ModelListener l : listeners) {
            if( l instanceof  OnStudentChangedListener) {
                OnStudentChangedListener listener = (OnStudentChangedListener) l;
                listener.onStudentChanged(student);
            }
        }
    }

    private void notifyClassChange(Klass klass) {
        for (ModelListener l : listeners) {
            if( l instanceof  OnClassChangedListener) {
                OnClassChangedListener listener = (OnClassChangedListener) l;
                listener.onClassChanged(klass);
            }
        }
    }

    private Model() {
        classes = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    private void notifyModelChange(){
        for (ModelListener l : listeners) {
            if( l instanceof  OnModelChangedListener) {
                OnModelChangedListener listener = (OnModelChangedListener)l;
                listener.onModelChanged();
            }
        }
    }

    public void addListener(ModelListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void saveModel() {
        try {
            Gson gson = new GsonBuilder().setVersion(Double.parseDouble(MODEL_VERSION)).create();
            try (OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(MODEL_ROOT_NAME+MODEL_VERSION, MODE_PRIVATE))) {
                String s = gson.toJson(classes);
                writer.write(s.replace("\\\\", "\\"));
            }
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    public void loadModel() {
        try {
            String modelVersion = getModelVersion();
            classes =  readJsonStream(context.openFileInput(MODEL_ROOT_NAME+modelVersion), modelVersion);
            if( ! MODEL_VERSION.equals(modelVersion)) {
                migrateModelFrom(modelVersion);
            }
        } catch (FileNotFoundException e) {
            //- nothing to do, file doesn't exist
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private String getModelVersion() {
        File dir = context.getFilesDir();
        File[] files = dir.listFiles();
        for (File file : files) {
            if(file.getName().startsWith(MODEL_ROOT_NAME)) {
                return file.getName().substring(MODEL_ROOT_NAME.length());
            }
        }
        return "";
    }

    private List<Klass> readJsonStream(InputStream in, String version) throws IOException {
        Gson gson = getGson(version);
        Type collectionType = new TypeToken<List<Klass>>(){}.getType();
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"))) {
            return gson.fromJson(reader, collectionType);
        }
    }

    private Gson getGson(String version) {
        if(version.length() > 0) {
            return new GsonBuilder().setVersion(Double.parseDouble(version)).create();
        } else {
            //- no version found ... very first version
            return new GsonBuilder().setVersion(1.0).create();
        }
    }

    public int indexOf(Klass klass) {
        return classes.indexOf(klass);
    }

    public Student getStudent(String className, String firstName, String lastName) {
        Klass klass = getClass(className);
        return klass.getStudent(firstName, lastName);
    }

    public boolean removeStudent(String className, Student student) {
        Klass klass = getClass(className);
        boolean hasBeenRemoved = klass.removeStudent(student);
        if(hasBeenRemoved) {
            notifyClassChange(klass);
            saveModel();
        }
        return hasBeenRemoved;
    }

    public void renameStudent(Student student, String first, String last) {
        student.rename(first, last);
        notifyClassChange(student.getKlass());
        notifyStudentChange(student);
        saveModel();
    }

    public int getStudentPositionInHisClass(Student student) {
        return student.getKlass().getStudents().indexOf(student);
    }

    private void migrateModelFrom(String oldVersion) {
        if("1.0".equals(oldVersion)) {
            //- first version of model, no weight in student
            migrateModelFrom1To2();
        }
        //- once the model is up to date, we need to save it
        saveModel();
        //- remove old model file
        deleteOldModelFile(oldVersion);
    }

    private void migrateModelFrom1To2() {
        //- model 2.0 introduced weight field in student model
        for(Klass klass : classes) {
            klass.computeWeights();
        }
    }

    private void deleteOldModelFile(String oldVersion) {
        File dir = context.getFilesDir();
        File[] files = dir.listFiles();
        for (File file : files) {
            if(file.getName().equals(MODEL_ROOT_NAME+oldVersion)) {
                file.delete();
            }
        }
    }

    public void addStudentGrade(Student student, int studentGrade) {
        student.addGrade(studentGrade);
        student.getKlass().updateWeights(student);
        notifyStudentChange(student);
        saveModel();
    }

    public void resetGradesOf(Klass klass) {
        for (Student student : klass.getStudents()) {
            student.resetGrades();
            student.resetWeight();
        }
        notifyClassChange(klass);
        saveModel();
    }
}
