package com.spl2.givematch.service;

import com.spl2.givematch.dao.UserDAO;
import com.spl2.givematch.model.Role;
import com.spl2.givematch.model.User;
import com.spl2.givematch.util.PasswordUtil;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public User register(String name, String username, String plainPassword, Role role, String contactInfo) {
        if (userDAO.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setHashedPassword(PasswordUtil.hash(plainPassword));
        user.setRole(role);
        user.setContactInfo(contactInfo);
        user.setPriorityLevel(0);
        return userDAO.insert(user);
    }

    public Optional<User> login(String username, String plainPassword) {
        Optional<User> found = userDAO.findByUsername(username);
        if (found.isPresent() && PasswordUtil.verify(plainPassword, found.get().getHashedPassword())) {
            return found;
        }
        return Optional.empty();
    }
}
