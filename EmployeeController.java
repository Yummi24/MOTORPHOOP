package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private VBox employeeListContainer;

    private List<Employee> employees = new ArrayList<>();

    @FXML
    public void initialize() {
        loadEmployeeData();
    }

    private static class Employee {
        private final String id;
        private final String name;
        private final String position;

        public Employee(String id, String name, String position) {
            this.id = id;
            this.name = name;
            this.position = position;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getPosition() { return position; }
    }

    private void loadEmployeeData() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/Employees.csv"))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");
                if (details.length >= 12) {
                    Employee employee = new Employee(
                            details[0].trim(),
                            details[2].trim() + " " + details[1].trim(),
                            details[12].trim()
                    );
                    employees.add(employee);
                    employeeListContainer.getChildren().add(createEmployeeEntry(employee));
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading employee data: " + e.getMessage());
        }
    }

    private HBox createEmployeeEntry(Employee employee) {
        HBox box = new HBox(10);
        box.setStyle("-fx-border-color: #1f4a8d; -fx-border-width: 1; -fx-padding: 5;");

        box.getChildren().addAll(
                createLabel(employee.getId(), 80),
                createLabel(employee.getName(), 200),
                createLabel(employee.getPosition(), 200)
        );

        box.setOnMouseClicked(event -> showEmployeeDetails(employee));
        return box;
    }

    private Label createLabel(String text, int minWidth) {
        Label label = new Label(text);
        label.setMinWidth(minWidth);
        return label;
    }

    private void showEmployeeDetails(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeDetails.fxml"));
            Parent root = loader.load();

            EmployeeDetailsController controller = loader.getController();
            controller.displayEmployeeDetails(
                    employee.getId(),
                    employee.getName(),
                    "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A",
                    employee.getPosition(),
                    "N/A"
            );

            Stage stage = (Stage) employeeListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading EmployeeDetails.fxml: " + e.getMessage());
        }
    }

    public void setEmployeeID(String employeeID) {
        System.out.println("Employee ID received: " + employeeID);
        // Store the Employee ID if needed
    }


    @FXML private void handleEmployees(ActionEvent event) throws IOException { switchScene(event, "Employee.fxml"); }
    @FXML private void handleLeaveRequests(ActionEvent event) throws IOException { switchScene(event, "LeaveRequest.fxml"); }
    @FXML private void handleOTRequests(ActionEvent event) throws IOException { switchScene(event, "OTRequest.fxml"); }
    @FXML private void handleTimeStamps(ActionEvent event) throws IOException { switchScene(event, "TimeStamp.fxml"); }
    @FXML private void handleLogout(ActionEvent event) { switchScene(event, "Login.fxml"); }



    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading " + fxmlFile + ": " + e.getMessage());
        }
    }
}
