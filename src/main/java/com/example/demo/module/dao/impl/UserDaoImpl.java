package com.example.demo.module.dao.impl;

import com.example.demo.module.dao.UserDao;
import com.example.demo.module.model.User;
import com.example.demo.utils.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserDaoImpl implements UserDao {
    @Autowired
    private DBUtils dbUtils;
    @Override
    public List<Map<String, Object>> query(User user) {
        List<Map<String, Object>> users = dbUtils.queryListMap(user);
        return users;
    }
}
