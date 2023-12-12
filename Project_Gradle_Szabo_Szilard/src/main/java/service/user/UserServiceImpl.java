package service.user;

import model.Role;
import model.User;
import model.validator.Notification;
import repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByID(Long id) { return userRepository.findByID(id); }

    @Override
    public String getSalt(Long id) {
        return userRepository.getSalt(id);
    }

    @Override
    public boolean save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void removeAll() {
        this.userRepository.removeAll();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<User> findAllEmployees() {
        return userRepository.findAllEmployees();
    }

    @Override
    public void delete(Long id) {
        this.userRepository.delete(id);
    }

    @Override
    public void update(User user) {
        this.userRepository.update(user);
    }
}
