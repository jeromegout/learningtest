package com.jeromegout.learningtest.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Since;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jeromegout on 07/01/2018.
 *
 */

public class Student implements Comparable<Student> {

    private static final int VB_SCORE = 5;
    private static final int B_SCORE = 10;
    private static final int G_SCORE = 15;
    private static final int VG_SCORE = 20;

    private static final double DEFAULT_WEIGHT = 100.0;

    private String firstName;
    private String lastName;
    private String className;
    private List<Grade> grades;
    @Since(2.0)
    private double weight;

    public Student(String firstName, String lastName, String className) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
        this.grades = new ArrayList<>();
        this.weight = DEFAULT_WEIGHT;
    }

    public int getScore() {
        int veryBadNum=0;
        int badNum=0;
        int goodNum=0;
        int veryGoodNum=0;
        for (Grade grade : grades) {
            switch (grade.getGradeRank()) {
                case 0 : veryBadNum++; break;
                case 1 : badNum++; break;
                case 2 : goodNum++; break;
                case 3 : veryGoodNum++; break;
            }
        }
        if(grades.size() == 0) {
            //- not yet a score
            return -1;
        }
        return (veryBadNum*VB_SCORE+badNum*B_SCORE+goodNum*G_SCORE+veryGoodNum*VG_SCORE) / (veryBadNum + badNum + goodNum + veryGoodNum);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int compareTo(@NonNull Student other) {
        String fullName1 = getLastName().toLowerCase()+getFirstName().toLowerCase();
        String fullName2 = other.getLastName().toLowerCase()+other.getFirstName().toLowerCase();
        return fullName1.compareTo(fullName2);
    }

    void rename(String first, String last) {
        firstName = first;
        lastName = last;
        getKlass().update();
    }

    Klass getKlass() {
        return Model.instance.getClass(className);
    }

    void setClassName(String className) {
        this.className = className;
    }

    public List getLastGrades() {
        int size = grades.size();
        if(size == 0) return Collections.EMPTY_LIST;
        else {
            if(size <= 3) return grades;
            else {
                //- more than n grades, get n first since insertion is made by head
                return grades.subList(0, 3);
            }
        }
    }

    void addGrade(int grade) {
        grades.add(0, new Grade(grade));
    }

    public List<Grade> getGrades() {
        return grades;
    }

    void resetGrades() {
        grades = new ArrayList<>();
    }

    long getLastTime() {
        if(grades.size() > 0) {
            return grades.get(0).getDate();
        }
        return 0;
    }

    double getWeight() {
        return weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }

    void resetWeight() {
        this.weight = DEFAULT_WEIGHT;
    }
}
