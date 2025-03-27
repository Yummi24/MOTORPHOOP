package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeaveRequestController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static final String CSV_FILE = "src/Leave Request.csv"; // Make sure the path is correct

    @FXML
    private Button Employees, LeaveRequest, OTrequest, Timestamp, Logout, Approve, Decline;
    @FXML
    private TableView<LeaveRequest> leaveTable;
    @FXML
    private TableColumn<LeaveRequest, String> employeeIDColumn;
    @FXML
    private TableColumn<LeaveRequest, String> nameColumn;
    @FXML
    private TableColumn<LeaveRequest, String> positionColumn;
    @FXML
    private TableColumn<LeaveRequest, String> leaveDateColumn;  // This will now show merged Start & End Date
    @FXML
    private TableColumn<LeaveRequest, String> leaveTypeColumn;
    @FXML
    private TableColumn<LeaveRequest, String> statusColumn;

    private final List<LeaveRequest> leaveRequests = new ArrayList<>();

    public LeaveRequestController() {
        loadLeaveRequests();
    }

    @FXML
    public void initialize() {
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));  // ✅ Correct field
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        leaveDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));  // ✅ Merged Start & End Date
        leaveTypeColumn.setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        leaveTable.getItems().setAll(leaveRequests);
    }

    private void loadLeaveRequests() {
        File file = new File(CSV_FILE);
        System.out.println("Looking for file at: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());

        if (!file.exists()) {
            System.out.println("🚨 ERROR: CSV file NOT FOUND!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 7) { // ✅ Ensure correct number of columns
                    leaveRequests.add(new LeaveRequest(
                            data[0],  // Employee ID
                            data[1],  // Employee Name
                            data[2],  // Position
                            data[3],  // Leave Type
                            data[4],  // Start Date
                            data[5],  // End Date
                            data[6]   // Status
                    ));
                } else {
                    System.out.println("⚠️ Skipping invalid row: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading leave requests: " + e.getMessage());
        }
    }

    @FXML
    private void approveRequest(ActionEvent event) {
        LeaveRequest selectedRequest = leaveTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            if (showConfirmation("Approve", "Are you sure you want to approve this leave request?")) {
                selectedRequest.setStatus("Approved");
                saveLeaveRequests();
                leaveTable.refresh();
            }
        } else {
            showWarning("No Selection", "Please select a request to approve.");
        }
    }

    @FXML
    private void declineRequest(ActionEvent event) {
        LeaveRequest selectedRequest = leaveTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            if (showConfirmation("Decline", "Are you sure you want to decline this leave request?")) {
                selectedRequest.setStatus("Declined");
                saveLeaveRequests();
                leaveTable.refresh();
            }
        } else {
            showWarning("No Selection", "Please select a request to decline.");
        }
    }

    private void saveLeaveRequests() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            writer.write("Employee ID,Name,Position,Leave Type,Start Date,End Date,Status\n"); // Ensure header
            for (LeaveRequest request : leaveRequests) {
                writer.write(request.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving leave requests: " + e.getMessage());
        }
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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
