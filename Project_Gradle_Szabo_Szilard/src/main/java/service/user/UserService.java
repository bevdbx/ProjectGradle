package service.user;

import model.Role;
import model.User;
import model.validator.Notification;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface UserService{
    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
    Optional<User> findByID(Long id);

    String getSalt(Long id);

    //boolean save(User user, List<Role> roles);
    boolean save(User user);


    void removeAll();

    boolean existsByUsername(String username);

    List<User> findAllEmployees();

    void delete(Long id);

    void update(User user);
}
