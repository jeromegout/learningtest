package com.jeromegout.learningtest.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by jeromegout on 07/01/2018.
 *
 */

public class Klass {

    private String name;
    private List<Student> students;
    private final static Random random = new Random();

    Klass(String name) {
        this.name = name;
        students = new ArrayList<>();
    }

    void setName(String name) {
        this.name = name;
        //- change all student class
        for (Student student : students) {
            student.setClassName(name);
        }
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }

    void addStudent(Student student) {
        if(!students.contains(student)) {
            students.add(student);
        }
        update();
    }

    void update() {
        Collections.sort(students);
    }

    Student getStudent(String firstName, String lastName) {
        for (Student s : students) {
            if(s.getLastName().equals(lastName) && s.getFirstName().equals(firstName)) {
                return s;
            }
        }
        return null;
    }

    boolean removeStudent(Student student) {
        return students.remove(student);
    }

    public Student randomDraw() {
        double weightSum = 0;
        for(Student s : students) {
            weightSum += s.getWeight();
        }

        double r = random.nextDouble()*weightSum;

        //- find the chosen student according to its weight
        for(Student s : students) {
            r -= s.getWeight();
            if(r <= 0) return s;
        }
        return null;
    }

    class TimedGrade implements Comparable<TimedGrade> {
        long gradeDate;
        Student student;
        TimedGrade(long date, Student s){
            gradeDate = date;
            student = s;
        }
        @Override
        public int compareTo(@NonNull TimedGrade tg) {
            return (int)(gradeDate - tg.gradeDate);
        }
    }

    //- compute all student weights according to the dates of his/her grades
    void computeWeights() {
        List<TimedGrade> grades = new ArrayList<>();
        for(Student s : students){
            //- init weight
            s.resetWeight();
            //- add all grades of each student
            for(Grade g : s.getGrades()) {
                grades.add(new TimedGrade(g.getDate(), s));
            }
        }
        //- sort all grades according to its date
        Collections.sort(grades);
        //- replay update of weights
        for (TimedGrade tg : grades) {
            updateWeights(tg.student);
        }
    }

    void updateWeights(Student winner) {
        double deltaWeight = winner.getWeight() / 2.0;
        for(Student s : students) {
            if(s == winner) {
                s.setWeight(deltaWeight);
            } else {
                s.setWeight(s.getWeight()+(deltaWeight / students.size()-1));
            }
        }
    }
}
