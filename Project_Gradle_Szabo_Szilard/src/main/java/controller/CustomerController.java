package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.ComponentFactory;
import model.Book;
import model.User;
import view.CustomerView;
import view.LoginView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerController {

    private final CustomerView customerView;
    private final ComponentFactory componentFactory;

    private final User user;

    public CustomerController(CustomerView customerView, ComponentFactory componentFactory, User user) {

        this.customerView = customerView;
        this.componentFactory = componentFactory;
        this.user = user;

        this.customerView.addBuyButtonListener(new BuyButtonListener());
    }

    private class BuyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            Book book = customerView.getSelectedBook();
            book.setQuantity(book.getQuantity() - 1);
            componentFactory.getBookService().updateBook(book);
            customerView.loadBooksIntoTable(customerView.getTableView());
            Long id = customerView.getEmployeeDropdown();
            Optional<User> optionalUser = componentFactory.getUserService().findByID(id);

            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();

                Map<Long, Long> booksMap = new HashMap<>();

                booksMap.put(user.getId(), book.getId());
                Map<Long, Long> userBooks = user.getBooks();
                user.setBooks(booksMap);

                for (Map.Entry<Long, Long> entry : user.getBooks().entrySet()) {
                    System.out.println("Book ID: " + entry.getKey() + ", Quantity: " + entry.getValue());
                }
            }
        }
    }
}
