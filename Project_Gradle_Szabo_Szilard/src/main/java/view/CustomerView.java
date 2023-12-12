package view;

import database.Constants;
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
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CustomerView {

    private final ComponentFactory componentFactory;
    private Button buyButton;
    private ComboBox<Long> employeeDropdown;
    private TableView<Book> tableView;

    public CustomerView(Stage primaryStage, ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
        primaryStage.setTitle("Book Store");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        tableView = createBookTableView();
        gridPane.add(tableView, 0, 1);

        Label label = new Label("Choose Employee from whom to buy the book:");
        gridPane.add(label, 0, 2);

        employeeDropdown = createEmployeeDropdown();
        gridPane.add(employeeDropdown, 1, 2);

        buyButton = new Button("Buy");
        gridPane.add(buyButton, 2, 2);

        loadBooksIntoTable(tableView);

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

    private TableView<Book> createBookTableView() {
        TableView<Book> tableView = new TableView<>();

        TableColumn<Book, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> publishedDateColumn = new TableColumn<>("Published Date");
        publishedDateColumn.setPrefWidth(90);
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        //publishedDateColumn.setPrefWidth(80);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, publishedDateColumn, quantityColumn);


        return tableView;
    }

    private ComboBox<Long> createEmployeeDropdown() {
        employeeDropdown = new ComboBox<>();
        List<Long> employees = new ArrayList<>();
        List<User> allUsers = componentFactory.getUserService().findAll();

        // Filter users with the role of employee
        for (User user : allUsers) {
            if (user.getRoles().stream().anyMatch(employeeRole -> employeeRole.getRole().equals(Constants.Roles.EMPLOYEE))) {
                employees.add(user.getId());
            }
        }

        ObservableList<Long> employeeList = FXCollections.observableArrayList(employees);
        employeeDropdown.setItems(employeeList);
        return employeeDropdown;
    }

    public void loadBooksIntoTable(TableView<Book> tableView) {
        List<Book> books = componentFactory.getBookService().findAll();
        ObservableList<Book> data = FXCollections.observableArrayList(books);
        tableView.setItems(data);
    }
    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener) {
        buyButton.setOnAction(buyButtonListener);
    }

    public Long getEmployeeDropdown() {
        return employeeDropdown.getValue();
    }

    public Book getSelectedBook() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    public TableView<Book> getTableView() {
        return tableView;
    }
}
