package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class OTRequestController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static final String CSV_FILE = "src/OT Request.csv";

    @FXML
    private Button Employees;
    @FXML
    private Button Leaverequest;
    @FXML
    private Button OTrequest;
    @FXML
    private Button Timestamp;
    @FXML
    private Button Logout;
    @FXML
    private Button Approve;
    @FXML
    private Button Decline;
    @FXML
    private TableView<OTRequest> otTable;
    @FXML
    private TableColumn<OTRequest, String> employeeIDColumn;
    @FXML
    private TableColumn<OTRequest, String> nameColumn;
    @FXML
    private TableColumn<OTRequest, String> positionColumn;
    @FXML
    private TableColumn<OTRequest, String> otDateColumn;
    @FXML
    private TableColumn<OTRequest, String> startTimeColumn;
    @FXML
    private TableColumn<OTRequest, String> endTimeColumn;
    @FXML
    private TableColumn<OTRequest, String> hoursColumn;
    @FXML
    private TableColumn<OTRequest, String> reasonColumn;
    @FXML
    private TableColumn<OTRequest, String> statusColumn;

    private List<OTRequest> otRequests = new ArrayList<>();

    public OTRequestController() {
        loadOTRequests();
    }

    @FXML
    public void initialize() {
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        otDateColumn.setCellValueFactory(new PropertyValueFactory<>("otDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        hoursColumn.setCellValueFactory(new PropertyValueFactory<>("hours"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        otTable.getItems().setAll(otRequests);
    }

    private void loadOTRequests() {
        File file = new File(CSV_FILE);
        System.out.println("Looking for file at: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());

        if (!file.exists()) {
            System.out.println("Error: CSV file not found - " + CSV_FILE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Skip the header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 9) {
                    otRequests.add(new OTRequest(
                            data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading OT requests: " + e.getMessage());
        }
    }


    @FXML
    private void approveRequest(ActionEvent event) {
        OTRequest selectedRequest = otTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Approval");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to approve this request?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedRequest.setStatus("Approved");
                saveOTRequests();
                otTable.refresh();
            }
        } else {
            showWarning("No Selection", "Please select a request to approve.");
        }
    }

    @FXML
    private void declineRequest(ActionEvent event) {
        OTRequest selectedRequest = otTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Decline");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to decline this request?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedRequest.setStatus("Declined");
                saveOTRequests();
                otTable.refresh();
            }
        } else {
            showWarning("No Selection", "Please select a request to decline.");
        }
    }

    private void saveOTRequests() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (OTRequest request : otRequests) {
                writer.write(request.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving OT requests: " + e.getMessage());
        }
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
