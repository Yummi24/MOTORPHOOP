package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class TimeStampController {

    @FXML private VBox attendanceContainer;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize() {
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        final int RECORD_LIMIT = 50; // Limit records for better performance
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("src/Attendance Record.csv"))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null && count < RECORD_LIMIT) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");
                if (details.length >= 6) {
                    attendanceContainer.getChildren().add(createAttendanceBox(details));
                    count++;
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error loading attendance record data: " + e.getMessage());
        }
    }

    private HBox createAttendanceBox(String[] details) {
        HBox box = new HBox(10);
        box.setStyle("-fx-border-color: #1f4a8d; -fx-border-width: 1; -fx-padding: 5;");

        box.getChildren().addAll(
                createLabel(details[0], 80),
                createLabel(details[1] + " " + details[2], 200),
                createLabel(details[3], 120),
                createLabel(details[4], 80),
                createLabel(details[5], 80),
                createLabel(calculateTotalHours(details[4], details[5]), 100)
        );

        return box;
    }

    private Label createLabel(String text, int minWidth) {
        Label label = new Label(text);
        label.setMinWidth(minWidth);
        return label;
    }

    private String calculateTotalHours(String logIn, String logOut) {
        try {
            String[] logInTime = logIn.split(":");
            String[] logOutTime = logOut.split(":");

            int totalMinutes = (Integer.parseInt(logOutTime[0]) * 60 + Integer.parseInt(logOutTime[1])) -
                    (Integer.parseInt(logInTime[0]) * 60 + Integer.parseInt(logInTime[1]));

            return String.format("%02d:%02d", totalMinutes / 60, totalMinutes % 60);
        } catch (Exception e) {
            return "N/A";
        }
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
    private void handlePayslip(ActionEvent event) throws IOException {
        switchScene(event, "Payslip.fxml");
    }

    @FXML
    private void handleEdit(ActionEvent event) throws IOException {
        switchScene(event, "EmployeeEDIT.fxml");
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
