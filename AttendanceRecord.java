package com.example.motorphoop;

public class AttendanceRecord {
    private String employeeId;
    private String date;
    private String hoursWorked;

    public AttendanceRecord(String employeeId, String date, String hoursWorked) {
        this.employeeId = employeeId;
        this.date = date;
        this.hoursWorked = hoursWorked;
    }

    public String getHoursWorked() {
        return hoursWorked;
    }
}
