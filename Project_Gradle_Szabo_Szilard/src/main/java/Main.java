

import controller.LoginController;
import database.Constants;
import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Book;
import model.Right;
import model.Role;
import model.User;
import model.builder.BookBuilder;
import model.validator.UserValidator;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;
import view.LoginView;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static database.Constants.Schemas.PRODUCTION;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
        /*
        BookRepository bookRepository = new BookRepositoryCacheDecorator(
                new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection()),
                new Cache<>()
        );

        BookService bookService = new BookServiceImpl(bookRepository);

        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();

        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        AuthenticationService authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRolesRepository);

        if (userRepository.existsByUsername("Alex")) {
            System.out.println("Username already exists in user table");
        } else {
            authenticationService.register("Alex", "parola123");
        }

        System.out.println(authenticationService.login("Alex", "parola123"));

         */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Connection connection = new JDBConnectionWrapper(PRODUCTION).getConnection();

        final RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        final UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        final AuthenticationService authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRolesRepository);

        final LoginView loginView = new LoginView(primaryStage);

        new LoginController(loginView, authenticationService);
    }

}
