package com.example.motorphoop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AttendanceDetailsController {

    private static final String CSV_FILE = "src/Attendance Record.csv";
    private static final Map<String, String[]> employeeAttendanceData = new HashMap<>();

    public static void loadAttendanceData() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { // Skip header
                    firstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 6) {
                    String employeeID = values[0];
                    String totalHours = values[5];  // Assuming column 6 stores total hours

                    // Check if employee already exists, update total hours worked and days worked
                    if (employeeAttendanceData.containsKey(employeeID)) {
                        String[] data = employeeAttendanceData.get(employeeID);
                        int daysWorked = Integer.parseInt(data[0]) + 1;
                        double hoursWorked = Double.parseDouble(data[1]) + Double.parseDouble(totalHours);
                        employeeAttendanceData.put(employeeID, new String[]{String.valueOf(daysWorked), String.valueOf(hoursWorked)});
                    } else {
                        employeeAttendanceData.put(employeeID, new String[]{"1", totalHours});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Map<String, String[]> getEmployeeAttendanceData() {
        return employeeAttendanceData;
    }
}
