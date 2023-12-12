package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.builder.BookBuilder;
import view.EmployeeView;

import java.time.LocalDate;
import java.util.Optional;


public class EmployeeController {

    private final EmployeeView employeeView;
    private final ComponentFactory componentFactory;

    public EmployeeController(EmployeeView employeeView, ComponentFactory componentFactory) {
        this.employeeView = employeeView;
        this.componentFactory = componentFactory;

        this.employeeView.addCreateBookButtonListener(new CreateBookButtonListener());
        this.employeeView.addDeleteBookButtonListener(new DeleteBookButtonListener());
        this.employeeView.addUpdateBookButtonListener(new UpdateBookButtonListener());
    }

    private class CreateBookButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            // Retrieve book details from the view
            String author = employeeView.getAuthorText();
            String title = employeeView.getTitleText();
            LocalDate publishDate = employeeView.getPublishDate();
            int quantity = employeeView.getQuantity();

            // Validate input
            if (author.isEmpty() || title.isEmpty() || publishDate == null || quantity <= 0) {
                showAlert("Invalid Input", "Please enter valid book details.");
                return;
            }

            // Create a new book
            Book newBook = new BookBuilder()
                    .setAuthor(author)
                    .setTitle(title)
                    .setPublishedDate(publishDate)
                    .setQuantity(quantity)
                    .build();

            // Save the new book
            boolean success = componentFactory.getBookService().save(newBook);

            if (success) {
                showAlert("Book Created", "The book has been created successfully.");
                employeeView.loadBooksIntoTable(employeeView.getTableView());
            } else {
                showAlert("Error", "An error occurred while creating the book.");
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

    private class DeleteBookButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getTableView().getSelectionModel().getSelectedItem();

            if (selectedBook != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete the selected book?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    componentFactory.getBookService().deleteBook(selectedBook.getId());

                    employeeView.loadBooksIntoTable(employeeView.getTableView());
                }
            } else {
                showAlert("No Book Selected", "Please select a book to delete.");
            }
        }
    }

    private class UpdateBookButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getTableView().getSelectionModel().getSelectedItem();

            String author = employeeView.getAuthorText();
            String title = employeeView.getTitleText();
            LocalDate publishDate = employeeView.getPublishDate();
            int quantity = employeeView.getQuantity();

            if (selectedBook != null) {
                selectedBook.setAuthor(author);
                selectedBook.setTitle(title);
                selectedBook.setPublishedDate(publishDate);
                selectedBook.setQuantity(quantity);
                componentFactory.getBookService().updateBook(selectedBook);
                employeeView.loadBooksIntoTable(employeeView.getTableView());

            } else {
                showAlert("No Book Selected", "Please select a book to update.");
            }

        }
    }
}
