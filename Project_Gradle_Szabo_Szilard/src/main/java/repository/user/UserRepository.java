package repository.user;

import model.Role;
import model.User;
import model.validator.Notification;

import java.util.*;

public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);

    Optional<User> findByID(Long id);

    String getSalt(Long id);

    void delete(Long id);

    void update(User user);

    boolean save(User user);

    //boolean save(User user);

    void removeAll();

    boolean existsByUsername(String username);

    List<User> findAllEmployees();
}
