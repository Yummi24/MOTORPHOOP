package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PayslipController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private Text employeeNameLabel;
    @FXML private Text employeePositionLabel;
    @FXML private Text employeeIDLabel;
    @FXML private TextArea payslipTextArea;
    @FXML private Text hoursWorkedText;
    @FXML private Text daysWorkedText;
    @FXML private Button generatePayslip;
    @FXML private ComboBox<String> monthSelector;

    private final Map<String, String[]> employeeSalaryData = new HashMap<>();
    private final Map<String, double[]> attendanceData = new HashMap<>();

    @FXML
    public void initialize() {
        monthSelector.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        // Set the default selected month to the current month
        monthSelector.setValue(LocalDateTime.now().getMonth().toString().charAt(0) +
                LocalDateTime.now().getMonth().toString().substring(1).toLowerCase());

        // Refresh attendance when the month is changed
        monthSelector.setOnAction(event -> {
            String employeeID = employeeIDLabel.getText().trim();
            if (!employeeID.isEmpty()) {
                loadAttendanceData(employeeID);
            }
        });
    }

    public void displayPayslipDetails(String id, String name, String position, double hoursWorked, int totalDays, String selectedMonth) {
        employeeIDLabel.setText(id);
        employeeNameLabel.setText(name);
        employeePositionLabel.setText(position);
        hoursWorkedText.setText(String.format("%.2f", hoursWorked));
        daysWorkedText.setText(String.valueOf(totalDays));
        monthSelector.setValue(selectedMonth);

        loadSalaryData();
        loadAttendanceData(id);
    }


    private void loadSalaryData() {
        employeeSalaryData.clear();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("Employees.csv"))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                String[] details = line.split(",");
                if (details.length >= 19) {
                    String employeeID = details[0].trim();
                    employeeSalaryData.put(employeeID, details);
                }
            }
        } catch (IOException e) {
            payslipTextArea.setText("❌ Error loading salary data.");
        }
    }

    private void loadAttendanceData(String employeeID) {
        attendanceData.clear();
        double totalHours = 0;
        int totalDays = 0;
        Set<String> uniqueDays = new HashSet<>();

        String selectedMonthName = monthSelector.getValue();
        int selectedMonth = convertMonthNameToNumber(selectedMonthName);

        try (BufferedReader br = Files.newBufferedReader(Paths.get("Attendance Record.csv"))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                String[] details = line.split(",");
                if (details.length >= 3) {
                    String id = details[0].trim();
                    if (!id.equals(employeeID)) continue;

                    try {
                        String datePart = details[1].split(" ")[0];
                        int recordMonth = Integer.parseInt(datePart.split("/")[0]);

                        if (recordMonth == selectedMonth) {
                            LocalDateTime loginTime = LocalDateTime.parse(details[1].trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
                            LocalDateTime logoutTime = LocalDateTime.parse(details[2].trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));

                            double hoursWorked = Duration.between(loginTime, logoutTime).toHours();
                            totalHours += hoursWorked;
                            uniqueDays.add(datePart);
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Error parsing timestamps for Employee ID: " + id);
                    }
                }
            }

            totalDays = uniqueDays.size();
            attendanceData.put(employeeID, new double[]{totalHours, totalDays});
        } catch (IOException e) {
            payslipTextArea.setText("❌ Error loading attendance data.");
        }

        hoursWorkedText.setText(String.format("%.2f", totalHours));
        daysWorkedText.setText(String.valueOf(totalDays));
    }



    private int convertMonthNameToNumber(String monthName) {
        return switch (monthName) {
            case "January" -> 1; case "February" -> 2; case "March" -> 3;
            case "April" -> 4; case "May" -> 5; case "June" -> 6;
            case "July" -> 7; case "August" -> 8; case "September" -> 9;
            case "October" -> 10; case "November" -> 11; case "December" -> 12;
            default -> 0;
        };
    }


    @FXML
    private void handleGeneratePayslip(ActionEvent event) {
        String employeeID = employeeIDLabel.getText().trim();
        if (!employeeSalaryData.containsKey(employeeID)) {
            payslipTextArea.setText("❌ Employee ID not found in salary data.");
            return;
        }
        String[] details = employeeSalaryData.get(employeeID);
        try {
            double basicSalary = Double.parseDouble(details[13]);
            double riceSubsidy = Double.parseDouble(details[14]);
            double phoneAllowance = Double.parseDouble(details[15]);
            double clothingAllowance = Double.parseDouble(details[16]);
            double hourlyRate = Double.parseDouble(details[18]);

            if (!attendanceData.containsKey(employeeID)) {
                payslipTextArea.setText("❌ No attendance records found for this employee.");
                return;
            }

            double totalHoursWorked = attendanceData.get(employeeID)[0];
            int totalDaysWorked = (int) attendanceData.get(employeeID)[1];

            double computedSalary = hourlyRate * totalHoursWorked;
            double totalEarnings = computedSalary + riceSubsidy + phoneAllowance + clothingAllowance;
            double totalDeductions = (basicSalary * 0.045) + (basicSalary * 0.035) + 100.00;
            double netPay = totalEarnings - totalDeductions;

            String payslip = String.format("""
MotorPH PaySlip
───────────────────────────
Employee Name : %s
Designation   : %s
Pay period    : %s 2024
Days worked   : %d
Hours worked  : %.2f
───────────────────────────
Earnings:
Basic Salary        : ₱ %.2f
Rice Subsidy        : ₱ %.2f
Phone Allowance     : ₱ %.2f
Clothing Allowance  : ₱ %.2f
Computed Salary     : ₱ %.2f
Total Earnings      : ₱ %.2f
───────────────────────────
Deductions:
SSS Contribution    : ₱ %.2f
PhilHealth          : ₱ %.2f
Pag-Ibig Fund       : ₱ %.2f
───────────────────────────
Net Pay             : ₱ %.2f
───────────────────────────
""",
                    employeeNameLabel.getText(),
                    employeePositionLabel.getText(),
                    monthSelector.getValue(),
                    totalDaysWorked,
                    totalHoursWorked,
                    basicSalary,
                    riceSubsidy,
                    phoneAllowance,
                    clothingAllowance,
                    computedSalary,
                    totalEarnings,
                    (basicSalary * 0.045),
                    (basicSalary * 0.035),
                    100.00,
                    netPay);

            payslipTextArea.setText(payslip);
        } catch (NumberFormatException e) {
            payslipTextArea.setText("❌ Error processing salary data.");
        }
    }


// Navigation Methods
    @FXML private void handleEmployees(ActionEvent event) throws IOException { switchScene(event, "Employee.fxml"); }
    @FXML private void handleLeaveRequests(ActionEvent event) throws IOException { switchScene(event, "LeaveRequest.fxml"); }
    @FXML private void handleOTRequests(ActionEvent event) throws IOException { switchScene(event, "OTRequest.fxml"); }
    @FXML private void handleTimeStamps(ActionEvent event) throws IOException { switchScene(event, "TimeStamp.fxml"); }
    @FXML private void handleEdit(ActionEvent event) throws IOException { switchScene(event, "EmployeeEDIT.fxml"); }

    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to confirm, or Cancel to stay logged in.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading Login.fxml. Check the file path.");
            }
        }
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void displayPayslipDetails(String employeeId, String name, String position, double attendanceDatum) {
    }
}
