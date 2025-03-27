package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private Stage stage;
    private Scene scene;

    @FXML
    public void initialize() {
        usernameField.setOnAction(this::onLoginButtonClick);
        passwordField.setOnAction(this::onLoginButtonClick);
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Username and password cannot be empty.");
            return;
        }

        // Authenticate user
        String[] userData = authenticateUser(username, password);

        if (userData != null) {
            String employeeID = userData[0];  // Extract Employee ID
            String role = userData[1];  // Extract Role
            switchDashboard(event, employeeID, role);  // Load appropriate dashboard
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    /**
     * Reads Users.csv to check login credentials
     */
    private String[] authenticateUser(String enteredUsername, String enteredPassword) {
        String filePath = "src/Users.csv"; // Update path if needed

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");
                if (details.length < 4) continue; // Ensure correct format

                String storedEmployeeID = details[0].trim();
                String storedUsername = details[1].trim();
                String storedPassword = details[2].trim();
                String storedRole = details[3].trim();

                if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                    return new String[]{storedEmployeeID, storedRole};
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading Users.csv: " + e.getMessage());
        }
        return null;
    }

    /**
     * Switches to Employee or HR dashboard based on role
     */
    private void switchDashboard(ActionEvent event, String employeeID, String role) {
        String fxmlFile = role.equalsIgnoreCase("HR") ? "Employee.fxml" : "EmployeeDashboard.fxml";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Set Employee ID to the correct controller
            if (role.equalsIgnoreCase("HR")) {
                EmployeeController controller = fxmlLoader.getController();
                controller.setEmployeeID(employeeID);  // ✅ Pass Employee ID to HR
            } else {
                EmployeeDashboardController controller = fxmlLoader.getController();
                controller.setEmployeeID(employeeID);  // ✅ Pass Employee ID to Regular Employee
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the dashboard.");
        }
    }

    /**
     * Displays an alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
