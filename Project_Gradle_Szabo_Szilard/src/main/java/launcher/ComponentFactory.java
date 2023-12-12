package launcher;

import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;
import view.LoginView;

import java.sql.Connection;

public class ComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepositoryMySQL bookRepository;
    private final BookService bookService;
    private static volatile ComponentFactory instance;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public BookRepositoryMySQL getBookRepository() {
        return bookRepository;
    }

    public static ComponentFactory getInstance() {
        return instance;
    }

    public static ComponentFactory getInstance(Boolean componentsForTests, Stage stage) {
        if(instance == null) {
            synchronized (ComponentFactory.class){
                if(instance == null){
                    instance = new ComponentFactory(componentsForTests, stage);
                }
            }
        }

        return instance;
    }

    public ComponentFactory(Boolean componentsForTests, Stage stage) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection,rightsRolesRepository);
        this.userService= new UserServiceImpl(userRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository,rightsRolesRepository);
        this.loginView = new LoginView(stage);
        this.loginController = new LoginController(loginView,this);
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public BookService getBookService() {
        return bookService;
    }

    public UserService getUserService() {
        return  userService;
    }

    public LoginController getLoginController() {
        return loginController;
    }
}
