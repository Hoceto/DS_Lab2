package com.example.demo.user_service;

import javassist.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserInterface {
    User addUser(User newUser);
    public void removeUser(UUID id) throws NotFoundException;
    public User getUserById(UUID id) throws NotFoundException;
    public List<User> getAllUsers();
}
