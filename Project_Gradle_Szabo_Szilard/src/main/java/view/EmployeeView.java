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
import model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmployeeView {

    private final ComponentFactory componentFactory;
    private Button createBookButton;
    private Button deleteBookButton;
    private Button updateBookButton;
    private Button activityButton;
    private TableView<Book> tableView;
    private TextField authorField;
    private TextField titleField;
    private DatePicker publishDateField;
    private TextField quantityField;

    public EmployeeView(Stage primaryStage, ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
        primaryStage.setTitle("Employee View");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        tableView = createBookTableView();
        gridPane.add(tableView, 0, 1);

        authorField = new TextField();
        titleField = new TextField();
        publishDateField = new DatePicker();
        quantityField = new TextField();

        createBookButton = new Button("Create");

        deleteBookButton = new Button("Delete");

        updateBookButton = new Button("Update");

        activityButton = new Button("Activity");

        gridPane.add(new Label("Author:"), 0, 2);
        gridPane.add(authorField, 1, 2);
        gridPane.add(new Label("Title:"), 0, 3);
        gridPane.add(titleField, 1, 3);
        gridPane.add(new Label("Publish Date:"), 0, 4);
        gridPane.add(publishDateField, 1, 4);
        gridPane.add(new Label("Quantity:"), 0, 5);
        gridPane.add(quantityField, 1, 5);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(createBookButton, deleteBookButton, updateBookButton, activityButton);
        gridPane.add(buttonBox, 1, 6);

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
        tableView = new TableView<>();

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
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, publishedDateColumn, quantityColumn);

        loadBooksIntoTable(tableView);

        tableView.setOnMouseClicked(event -> {
            Book selectedBook = tableView.getSelectionModel().getSelectedItem();

            if (selectedBook != null) {
                authorField.setText(selectedBook.getAuthor());
                titleField.setText(selectedBook.getTitle());
                publishDateField.setValue(selectedBook.getPublishedDate());
                quantityField.setText(String.valueOf(selectedBook.getQuantity()));
            }
        });

        return tableView;
    }

    public void addCreateBookButtonListener(EventHandler<ActionEvent> createBookButtonListener) {
        createBookButton.setOnAction(createBookButtonListener);
    }

    public void addDeleteBookButtonListener(EventHandler<ActionEvent> deleteBookButtonListener) {
        deleteBookButton.setOnAction(deleteBookButtonListener);
    }

    public void addUpdateBookButtonListener(EventHandler<ActionEvent> updateBookButtonListener) {
        updateBookButton.setOnAction(updateBookButtonListener);
    }

    public void addActivityButtonListener(EventHandler<ActionEvent> activityButtonListener) {
        activityButton.setOnAction(activityButtonListener);
    }

    public void loadBooksIntoTable(TableView<Book> tableView) {
        List<Book> books = componentFactory.getBookService().findAll();
        ObservableList<Book> data = FXCollections.observableArrayList(books);
        tableView.setItems(data);
    }


    public String getAuthorText() {
        return authorField.getText();
    }

    public String getTitleText() {
        return titleField.getText();
    }

    public LocalDate getPublishDate() {
        return publishDateField.getValue();
    }

    public int getQuantity() {
        try {
            return Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public TableView<Book> getTableView() {
        return tableView;
    }
}
