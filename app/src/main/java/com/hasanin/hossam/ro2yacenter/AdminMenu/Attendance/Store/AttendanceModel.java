package com.hasanin.hossam.ro2yacenter.AdminMenu.Attendance.Store;

import java.util.ArrayList;

public class AttendanceModel {
    public String subjectName;
    public int month;
    public int year;
    public long date;
    public int day;
    public ArrayList<String> attendantStudents;

    public AttendanceModel(){}

    public AttendanceModel(String subjectName , int month, int year, long date, int day, ArrayList<String> attendantStudents) {
        this.subjectName = subjectName;
        this.month = month;
        this.year = year;
        this.date = date;
        this.day = day;
        this.attendantStudents = attendantStudents;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<String> getAttendantStudents() {
        return attendantStudents;
    }

    public void setAttendantStudents(ArrayList<String> attendantStudents) {
        this.attendantStudents = attendantStudents;
    }
}
