package com.tcl.mp.controller;

import com.tcl.mp.entity.User;
import com.tcl.mp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author li
 * @version 1.0
 * @date 2020/8/6 19:57
 */
@RestController("/user")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/list")
    public List<User> userList() {
        return userMapper.selectList(null);
    }

}
