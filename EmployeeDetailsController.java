package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;

public class EmployeeDetailsController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private Text idLabel, nameLabel, birthdayLabel, addressLabel, phoneLabel,
            sssLabel, philhealthLabel, tinLabel, pagibigLabel, positionLabel, supervisorLabel;

    @FXML private Button Employees, Leaverequest, OTrequest, Timestamp, Logout, Payslip, Edit;

    private Map<String, String> selectedEmployee;

    // Set Selected Employee Data for Display
    public void setSelectedEmployee(Map<String, String> employeeData) {
        this.selectedEmployee = employeeData;

        idLabel.setText(employeeData.get("ID"));
        nameLabel.setText(employeeData.get("Name"));
        birthdayLabel.setText(employeeData.getOrDefault("Birthday", "N/A"));
        addressLabel.setText(employeeData.getOrDefault("Address", "N/A"));
        phoneLabel.setText(employeeData.getOrDefault("Phone", "N/A"));
        sssLabel.setText(employeeData.getOrDefault("SSS", "N/A"));
        philhealthLabel.setText(employeeData.getOrDefault("PhilHealth", "N/A"));
        tinLabel.setText(employeeData.getOrDefault("TIN", "N/A"));
        pagibigLabel.setText(employeeData.getOrDefault("Pagibig", "N/A"));
        positionLabel.setText(employeeData.get("Position"));
        supervisorLabel.setText(employeeData.getOrDefault("Supervisor", "N/A"));
    }

    // Navigation Methods with Data Passing
    @FXML
    private void handlePayslip(ActionEvent event) throws IOException {
        switchSceneWithEmployeeData(event, "Payslip.fxml");
    }

    @FXML
    private void handleEdit(ActionEvent event) throws IOException {
        switchSceneWithEmployeeData(event, "EmployeeEDIT.fxml");
    }

    private void switchSceneWithEmployeeData(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        root = loader.load();

        if (fxmlFile.equals("Payslip.fxml")) {
            PayslipController payslipController = loader.getController();
            payslipController.setSelectedEmployee(selectedEmployee);
        } else if (fxmlFile.equals("EmployeeEDIT.fxml")) {
            EmployeeEditController editController = loader.getController();
            editController.setSelectedEmployee(selectedEmployee);
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
