package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EmployeeDetailsController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Employee Detail Text Elements
    @FXML private Text idLabel;
    @FXML private Text nameLabel;
    @FXML private Text birthdayLabel;
    @FXML private Text addressLabel;
    @FXML private Text phoneLabel;
    @FXML private Text sssLabel;
    @FXML private Text philhealthLabel;
    @FXML private Text tinLabel;
    @FXML private Text pagibigLabel;
    @FXML private Text positionLabel;
    @FXML private Text supervisorLabel;

    // Navigation Buttons
    @FXML private Button Employees;
    @FXML private Button Leaverequest;
    @FXML private Button OTrequest;
    @FXML private Button Timestamp;
    @FXML private Button Logout;
    @FXML private Button Payslip;
    @FXML private Button Edit;

    // Display Employee Details
    public void displayEmployeeDetails(
            String id, String name, String birthday, String address, String phone,
            String sss, String philhealth, String tin, String pagibig,
            String position, String supervisor) {

        idLabel.setText(id);
        nameLabel.setText(name);
        birthdayLabel.setText(birthday);
        addressLabel.setText(address);
        phoneLabel.setText(phone);
        sssLabel.setText(sss);
        philhealthLabel.setText(philhealth);
        tinLabel.setText(tin);
        pagibigLabel.setText(pagibig);
        positionLabel.setText(position);
        supervisorLabel.setText(supervisor);
    }

    // Navigation Methods
    @FXML
    private void handleEmployees(ActionEvent event) throws IOException {
        switchScene(event, "Employee.fxml");
    }

    @FXML
    private void handleLeaveRequests(ActionEvent event) throws IOException {
        switchScene(event, "LeaveRequest.fxml");
    }

    @FXML
    private void handleOTRequests(ActionEvent event) throws IOException {
        switchScene(event, "OTRequest.fxml");
    }

    @FXML
    private void handleTimeStamps(ActionEvent event) throws IOException {
        switchScene(event, "TimeStamp.fxml");
    }

    @FXML
    private void handlePayslip(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payslip.fxml"));
        Parent root = loader.load();

        PayslipController payslipController = loader.getController();

        String employeeId = idLabel.getText();
        String name = nameLabel.getText();
        String position = positionLabel.getText();

        double[] attendanceData = fetchAttendanceHours(employeeId);

        payslipController.displayPayslipDetails(employeeId, name, position, attendanceData[0]);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Fetches total hours worked and days worked for the selected employee.
     * @param employeeId The employee's ID.
     * @return An array {totalHours, totalDays}.
     */
    private double[] fetchAttendanceHours(String employeeId) {
        double totalHours = 0;
        int totalDays = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        Set<String> uniqueDays = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader("Attendance Record.csv"))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                String[] details = line.split(",");
                if (details.length >= 3) {
                    String id = details[0].trim();
                    if (!id.equals(employeeId)) continue;

                    try {
                        LocalDateTime loginTime = LocalDateTime.parse(details[1].trim(), formatter);
                        LocalDateTime logoutTime = LocalDateTime.parse(details[2].trim(), formatter);

                        double hoursWorked = Duration.between(loginTime, logoutTime).toHours();
                        totalHours += hoursWorked;
                        uniqueDays.add(loginTime.toLocalDate().toString()); // Track unique days worked
                    } catch (Exception e) {
                        System.out.println("❌ Error parsing timestamps for Employee ID: " + id);
                    }
                }
            }
            totalDays = uniqueDays.size();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new double[]{totalHours, totalDays}; // Return both values
    }

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
}
