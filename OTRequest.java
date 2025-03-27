package com.example.motorphoop;

public class OTRequest {
    private String employeeID;
    private String name;
    private String position;
    private String otDate;
    private String startTime;
    private String endTime;
    private String hours;
    private String reason;
    private String status;

    public OTRequest(String employeeID, String name, String position, String otDate,
                     String startTime, String endTime, String hours, String reason, String status) {
        this.employeeID = employeeID;
        this.name = name;
        this.position = position;
        this.otDate = otDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hours = hours;
        this.reason = reason;
        this.status = status;
    }

    public String getEmployeeID() { return employeeID; }
    public void setEmployeeID(String employeeID) { this.employeeID = employeeID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getOtDate() { return otDate; }
    public void setOtDate(String otDate) { this.otDate = otDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getHours() { return hours; }
    public void setHours(String hours) { this.hours = hours; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toCSV() {
        return String.join(",", employeeID, name, position, otDate, startTime, endTime, hours, reason, status);
    }
}
