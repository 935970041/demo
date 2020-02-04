package com.example.demo.module.service.impl;

import com.example.demo.module.dao.UserDao;
import com.example.demo.module.model.User;
import com.example.demo.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public List<Map<String, Object>> query(User user) {
        return userDao.query(user);
    }
}
