package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Collections;

import static database.Constants.Roles.*;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password) {


        Role customerRole = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        String salt = saltGenerator();

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();
        Notification<Boolean> userRegisterNotification = new Notification<>();


        user = new UserBuilder()
                .setUsername(username)
                .setPassword(hashPassword(password,salt))
                .setRoles(Collections.singletonList(customerRole))
                .setSalt(salt)
                .build();


        if (!userValid) {
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else if (userRepository.existsByUsername(username)) {
            userRegisterNotification.addError("User already exists");
            userRegisterNotification.setResult(Boolean.FALSE);
        } else {
            userRegisterNotification.setResult(userRepository.save(user));
        }

        return userRegisterNotification;
    }

    @Override
    public Notification<User> login(String username, String password) {
        Notification<User> notification = new Notification<>();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            notification.addError("User not registered");
            return notification;
        }
        String salt = userRepository.getSalt(user.getId());
        return userRepository.findByUsernameAndPassword(username, hashPassword(password, salt));
    }

    @Override
    public boolean logout(User user) {
        return false;
    }



    private String hashPassword(String password, String salt) {
        try {
            // Sercured Hash Algorithm - 256
            // 1 byte = 8 biți
            // 1 byte = 1 char

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));

            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexStringBuilder = new StringBuilder(2 * hashedPassword.length);
            for (byte b : hashedPassword) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String saltGenerator() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        StringBuilder hexStringBuilder = new StringBuilder(2 * salt.length);
        for (byte b : salt) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }


}