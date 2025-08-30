package com.mmendoza.massemail.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.impl.xb.xsdschema.Attribute.Use;
import org.aspectj.apache.bcel.classfile.Module.Uses;
import org.springframework.stereotype.Service;

import com.mmendoza.massemail.model.User;
import com.mmendoza.massemail.repository.UserRepository;
import com.mmendoza.massemail.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveManyUser(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    public List<User> getRecordsByQuantity(Integer quantity) {
        return userRepository.getRecords(quantity);
    }

}
