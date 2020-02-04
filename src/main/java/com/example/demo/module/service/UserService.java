package com.example.demo.module.service;


import com.example.demo.module.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Map<String, Object>> query(User user);
}
