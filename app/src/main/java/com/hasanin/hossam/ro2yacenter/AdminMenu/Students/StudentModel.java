package com.hasanin.hossam.ro2yacenter.AdminMenu.Students;

import java.util.ArrayList;

public class StudentModel {

    public String code , studyGrade;
    public boolean isadmin;
    public ArrayList<String> subjects;
    public String name;
    public Long last_login;

    public StudentModel(){}

    public StudentModel(String code, String studyGrade, boolean isadmin, ArrayList<String> subjects, String name, Long last_login) {
        this.code = code;
        this.studyGrade = studyGrade;
        this.isadmin = isadmin;
        this.subjects = subjects;
        this.name = name;
        this.last_login = last_login;
    }

    public String getStudyGrade() {
        return studyGrade;
    }

    public void setStudyGrade(String studyGrade) {
        this.studyGrade = studyGrade;
    }

    public String getCode() {
        return code;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public String getName() {
        return name;
    }

    public Long getLast_login() {
        return last_login;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast_login(Long last_login) {
        this.last_login = last_login;
    }
}
