package com.hasanin.hossam.ro2yacenter.AdminMenu.Monthly;

import java.util.ArrayList;

public class MonthlyModel {
    public String subjectName;
    public int month , year;
    public ArrayList<String> usersCode;

    public MonthlyModel(){}

    public MonthlyModel(String subjectName, int month, int year, ArrayList<String> usersCode) {
        this.subjectName = subjectName;
        this.month = month;
        this.year = year;
        this.usersCode = usersCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<String> getUsersCode() {
        return usersCode;
    }

    public void setUsersCode(ArrayList<String> usersCode) {
        this.usersCode = usersCode;
    }
}
