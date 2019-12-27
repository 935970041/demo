package com.example.demo.module.controller;

import com.example.demo.module.model.User;
import com.example.demo.utils.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class HelloController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DBUtils dBUtils;
    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello world" ;
    }

    @RequestMapping("jumpIndex")
    public String jumpIndex(){
        return "index" ;
    }
    @RequestMapping("getData")
    @ResponseBody
    public String getData(){
        String sql="select * from user";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps.toString();
    }
    @RequestMapping("getUserData")
    @ResponseBody
    public String getUserData(){
        User user=new User();
        //user.setId("1");
        List<Map<String, Object>> maps=dBUtils.queryListMap(user);
        return maps.toString();
    }
}
