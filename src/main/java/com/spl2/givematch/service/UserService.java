package com.spl2.givematch.service;

import com.spl2.givematch.dao.UserDAO;
import com.spl2.givematch.model.Role;
import com.spl2.givematch.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public Optional<User> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public Optional<User> findById(int id) {
        return userDAO.findById(id);
    }

    public List<User> findReceivers() {
        return userDAO.findByRole(Role.RECEIVER);
    }

    public List<User> findDonors() {
        return userDAO.findByRole(Role.DONOR);
    }

    public void setPriorityLevel(int userId, int priorityLevel) {
        userDAO.updatePriorityLevel(userId, priorityLevel);
    }
}
