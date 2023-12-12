// AdminView.java
package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.User;

import java.util.List;
import java.util.Optional;

public class AdminView {

    private final ComponentFactory componentFactory;
    private Button createEmployeeButton;
    private Button deleteEmployeeButton;
    private Button updateEmployeeButton;
    private TableView<User> employeeTableView;
    private TextField usernameField;
    private TextField passwordField;

    public AdminView(Stage primaryStage, ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
        primaryStage.setTitle("Admin View");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        employeeTableView = createEmployeeTableView();
        gridPane.add(employeeTableView, 0, 1);

        usernameField = new TextField();
        passwordField = new TextField();

        createEmployeeButton = new Button("Create Employee");
        deleteEmployeeButton = new Button("Delete Employee");
        updateEmployeeButton = new Button("Update Employee");

        gridPane.add(new Label("Username:"), 0, 2);
        gridPane.add(usernameField, 1, 2);
        gridPane.add(new Label("Password:"), 0, 3);
        gridPane.add(passwordField, 1, 3);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(createEmployeeButton, deleteEmployeeButton, updateEmployeeButton);
        gridPane.add(buttonBox, 1, 4);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private TableView<User> createEmployeeTableView() {
        TableView<User> tableView = new TableView<>();

        TableColumn<User, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setMinWidth(150);  // Set a fixed width for the password column

        tableView.getColumns().addAll(idColumn, usernameColumn, passwordColumn);

        loadEmployeesIntoTable(tableView);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setOnMouseClicked(event -> {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                usernameField.setText(selectedUser.getUsername());
                passwordField.setText(selectedUser.getPassword());
            }
        });

        return tableView;
    }

    public void addCreateEmployeeButtonListener(EventHandler<ActionEvent> createEmployeeButtonListener) {
        createEmployeeButton.setOnAction(createEmployeeButtonListener);
    }

    public void addDeleteEmployeeButtonListener(EventHandler<ActionEvent> deleteEmployeeButtonListener) {
        deleteEmployeeButton.setOnAction(deleteEmployeeButtonListener);
    }

    public void addUpdateEmployeeButtonListener(EventHandler<ActionEvent> updateEmployeeButtonListener) {
        updateEmployeeButton.setOnAction(updateEmployeeButtonListener);
    }

    public void loadEmployeesIntoTable(TableView<User> tableView) {
        List<User> employees = componentFactory.getUserService().findAllEmployees();
        ObservableList<User> data = FXCollections.observableArrayList(employees);
        tableView.setItems(data);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public User getUser() {
        return employeeTableView.getSelectionModel().getSelectedItem();
    }

    public TableView<User> getEmployeeTableView() {
        return employeeTableView;
    }
}
