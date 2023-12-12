package controller;

import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.Role;
import model.User;
import model.validator.Notification;
import model.validator.UserValidator;
import service.user.AuthenticationService;
import view.AdminView;
import view.CustomerView;
import view.EmployeeView;
import view.LoginView;

import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final ComponentFactory componentFactory;

    public LoginController(LoginView loginView, ComponentFactory componentFactory) {

        this.loginView = loginView;
        this.componentFactory = componentFactory;

        this.loginView.addLoginButtonListener(new LogicButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LogicButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

            String username = loginView.getUsername();
            String password = loginView.getPassword();
            Notification<User> loginNotification = componentFactory.getAuthenticationService().login(username,password);

            if (loginNotification.hasError()) {
                if (loginNotification.getFormattedErrors().contains("Invalid username or password!")) {
                    loginView.setActionTargetText("Invalid username or password. Please try again.");
                } else {
                    loginView.setActionTargetText(loginNotification.getFormattedErrors());
                }
            } else {
                loginView.setActionTargetText("Login successful!");
                openViewBasedOnRole(loginNotification.getResult());
            }
        }
    }

    private void openViewBasedOnRole(User user) {
        List<Role> roles = componentFactory.getRightsRolesRepository().findRolesForUser(user.getId());
        for(Role role: roles) {
            System.out.println(role);
        }
        if (roles.stream().anyMatch(role -> "administrator".equals(role.getRole()))) {
            AdminView adminView = new AdminView(new Stage(), componentFactory);
            AdminController adminController = new AdminController(adminView, componentFactory);
        } else if (roles.stream().anyMatch(role -> "employee".equals(role.getRole()))) {
            EmployeeView employeeView = new EmployeeView(new Stage(), componentFactory);
            EmployeeController employeeController = new EmployeeController(employeeView, componentFactory);
        } else if (roles.stream().anyMatch(role -> "customer".equals(role.getRole()))) {
            CustomerView customerView = new CustomerView(new Stage(), componentFactory);
            CustomerController customerController = new CustomerController(customerView, componentFactory, user);
        } else {
            //System.out.println("Unknown user role: " + user.getRoles());
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = componentFactory.getAuthenticationService().register(username, password);

            if(registerNotification.hasError()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }

    }
}
