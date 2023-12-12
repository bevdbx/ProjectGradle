package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import launcher.ComponentFactory;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import view.AdminView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AdminController {

    private final AdminView adminView;
    private final ComponentFactory componentFactory;

    public AdminController(AdminView adminView, ComponentFactory componentFactory) {
        this.adminView = adminView;
        this.componentFactory = componentFactory;

        this.adminView.addCreateEmployeeButtonListener(new CreateEmployeeButtonListener());
        this.adminView.addDeleteEmployeeButtonListener(new DeleteEmployeeButtonListener());
        this.adminView.addUpdateEmployeeButtonListener(new UpdateEmployeeButtonListener());
    }

    private class CreateEmployeeButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Invalid Input", "Please enter valid Employee details.");
                return;
            }

            // Încercați să înregistrați un nou utilizator
            Notification<Boolean> registerNotification = componentFactory.getAuthenticationService().register(username, password);

            if (registerNotification.hasError()) {
                showAlert("Error", registerNotification.getFormattedErrors());
            } else {
                Optional<User> newUser = componentFactory.getUserService().findByUsername(username);

                newUser.ifPresent(user -> {
                    Role employeeRole = componentFactory.getRightsRolesRepository().findRoleByTitle("employee");

                    if (employeeRole != null) {
                        componentFactory.getRightsRolesRepository().addRolesToUser(user, Collections.singletonList(employeeRole));

                        showAlert("Employee Created", "The Employee has been created successfully.");
                        adminView.loadEmployeesIntoTable(adminView.getEmployeeTableView());
                    } else {
                        showAlert("Role Not Found", "The 'employee' role was not found.");
                    }
                });
            }
        }
    }

    private class DeleteEmployeeButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            User selectedUser = adminView.getUser();

            if (selectedUser != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete the selected Employee?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    componentFactory.getUserService().delete(selectedUser.getId());

                    adminView.loadEmployeesIntoTable(adminView.getEmployeeTableView());
                }
            } else {
                showAlert("No Employee Selected", "Please select a Employee to delete.");
            }
        }
    }

    private class UpdateEmployeeButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            User selectedUser = adminView.getEmployeeTableView().getSelectionModel().getSelectedItem();

            String username = adminView.getUsername();
            String password = adminView.getPassword();

            if (selectedUser != null) {
                selectedUser.setUsername(username);
                selectedUser.setPassword(password);
                componentFactory.getUserService().update(selectedUser);
                adminView.loadEmployeesIntoTable(adminView.getEmployeeTableView());

            } else {
                showAlert("No Employee Selected", "Please select a Employee to update.");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}



