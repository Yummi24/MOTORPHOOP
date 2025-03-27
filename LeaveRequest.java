package com.example.motorphoop;

public class LeaveRequest {
    private String employeeID;
    private String employeeName;
    private String position;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String status;

    public LeaveRequest(String employeeID, String employeeName, String position, String leaveType, String startDate, String endDate, String status) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.position = position;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // ✅ Getters
    public String getEmployeeID() { return employeeID; }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }
    public String getLeaveType() { return leaveType; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getStatus() { return status; }

    // ✅ Setter for status (allows updating status)
    public void setStatus(String status) { this.status = status; }

    // ✅ Method to merge Start Date and End Date for the table
    public String getDate() {
        return startDate + " - " + endDate;
    }

    // ✅ Converts the object to a CSV row format
    public String toCSV() {
        return employeeID + "," + employeeName + "," + position + "," + leaveType + "," + startDate + "," + endDate + "," + status;
    }
}
