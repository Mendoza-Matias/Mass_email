package com.mmendoza.massemail.service;

import java.util.List;

import com.mmendoza.massemail.model.User;

public interface UserService {
    void saveManyUser(List<User> users);
    List<User> getRecordsByQuantity(Integer quantity);
}
