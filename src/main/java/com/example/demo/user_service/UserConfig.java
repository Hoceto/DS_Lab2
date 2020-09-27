package com.example.demo.user_service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class UserConfig implements UserInterface{

    @Autowired
    private UserRepository repository;

    public User addUser(User newUser){
        return repository.save(newUser);
    }


    public void removeUser(UUID id) throws NotFoundException {
        User toRemove = getUserById(id);
        repository.delete(toRemove);
    }

    public User getUserById(UUID id) throws NotFoundException {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()){
            return user.get();
        }
        else {
            throw new NotFoundException(String.format("User ID %s does not exist", id));
        }
    }

    public List<User> getAllUsers(){
        List<User> userList = (List<User>) repository.findAll();

        return userList;
    }

    private static boolean validateName(String name) {
        if (name.matches("[A-Z][a-z]*")) {
            return true;
        } else {
            throw new IllegalArgumentException("Incorrect name format (must contain 1-inf a-z characters, first capital");
        }
    }

    private static boolean validateSurname(String surname) {
        if (surname.matches("[A-Z][a-z]*")) {
            return true;
        } else {
            throw new IllegalArgumentException("Incorrect surname format (must contain 1-inf a-z characters, first capital");
        }
    }

    public void changeBalance(UUID id, int diff) throws NotFoundException {
        User user = getUserById(id);
        user.setUserBalance(user.getUserBalance() + diff);
    }

}
