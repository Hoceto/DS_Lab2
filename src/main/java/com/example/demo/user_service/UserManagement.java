package com.example.demo.user_service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public final class UserManagement {
    private Map<UUID, User> user_storage;

    public UserManagement() {
        user_storage = new HashMap<>();
    }

    void addUser(User user) {
        this.user_storage.put(user.getUserId(), user);
    }

    void removeUser(UUID uuid) {
        this.user_storage.remove(uuid);
    }

    User getUser(UUID uuid) {
        return (User)this.user_storage.get(uuid);
    }

    Map<UUID, User> getUserStorage() {
        return this.user_storage;
    }
}