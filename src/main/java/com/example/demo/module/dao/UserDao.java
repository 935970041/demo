package com.example.demo.module.dao;


import com.example.demo.module.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    List<Map<String, Object>> query(User user);
}
