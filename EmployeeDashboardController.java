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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class EmployeeDashboardController {

    @FXML private Label nameLabel;
    @FXML private Label employeeIDLabel;
    @FXML private Label positionLabel;
    @FXML private Label netSalaryLabel;
    @FXML private Label phoneLabel;
    @FXML private Label statusLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label addressLabel;
    @FXML private Label tinLabel;
    @FXML private Label sssLabel;
    @FXML private Label philhealthLabel;
    @FXML private Label pagibigLabel;
    @FXML private Label supervisorLabel;

    @FXML private AnchorPane profilePane;
    @FXML private AnchorPane requestPane;

    private String loggedInEmployeeID;

    public void setEmployeeID(String employeeID) {
        this.loggedInEmployeeID = employeeID;
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        String csvFile = "src/Employees.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");

                if (details.length < 19) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String employeeID = details[0].trim();
                if (employeeID.equals(loggedInEmployeeID)) {
                    String lastName = details[1].trim();
                    String firstName = details[2].trim();
                    String fullName = firstName + " " + lastName;

                    // Explicit labels for better clarity
                    nameLabel.setText("Name: " + fullName);
                    employeeIDLabel.setText("Employee ID: " + employeeID);
                    positionLabel.setText("Position: " + details[11].trim());
                    netSalaryLabel.setText("Net Salary: " + details[13].trim());
                    phoneLabel.setText("Phone: " + details[5].trim());
                    statusLabel.setText("Status: " + details[10].trim());
                    birthdayLabel.setText("Birthday: " + details[3].trim());
                    addressLabel.setText("Address: " + details[4].trim());
                    tinLabel.setText("TIN: " + details[8].trim());
                    sssLabel.setText("SSS: " + details[6].trim());
                    philhealthLabel.setText("PhilHealth: " + details[7].trim());
                    pagibigLabel.setText("Pag-IBIG: " + details[9].trim());
                    supervisorLabel.setText("Supervisor: " + details[12].trim());

                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading employee data: " + e.getMessage());
        }
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

    @FXML
    private void switchToRequestPane() {
        profilePane.setVisible(false);
        requestPane.setVisible(true);
    }

    @FXML
    private void switchToProfilePane() {
        requestPane.setVisible(false);
        profilePane.setVisible(true);
    }
}
