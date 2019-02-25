package com.hasanin.hossam.ro2yacenter.AdminMenu.Subjects;

import java.util.ArrayList;

/**
 * Created by mohamed on 13/07/2018.
 */

public class SubjectModel {
    public String subjectId;
    public String subjectName;
    public String subjectTeacher;
    public ArrayList<String> subjectDays;
    public String subjecMoney;
    public String subjectTime;

    public  SubjectModel(){}

    public SubjectModel(String subjectId , String subjectName, String subjectTeacher, ArrayList<String> subjectDays, String subjecMoney, String subjectTime) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectTeacher = subjectTeacher;
        this.subjectDays = subjectDays;
        this.subjecMoney = subjecMoney;
        this.subjectTime = subjectTime;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setSubjectName(String subjectName) {

        this.subjectName = subjectName;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public void setSubjectDays(ArrayList<String> subjectDays) {
        this.subjectDays = subjectDays;
    }

    public void setSubjecMoney(String subjecMoney) {
        this.subjecMoney = subjecMoney;
    }

    public void setSubjectTime(String subjectTime) {
        this.subjectTime = subjectTime;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {

        return subjectName;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public ArrayList<String> getSubjectDays() {
        return subjectDays;
    }

    public String getSubjecMoney() {
        return subjecMoney;
    }

    public String getSubjectTime() {
        return subjectTime;
    }
}
