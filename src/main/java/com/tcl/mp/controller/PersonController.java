package com.tcl.mp.controller;

import com.tcl.mp.entity.Person;
import com.tcl.mp.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author li
 * @version 1.0
 * @date 2020/8/6 19:57
 */
@RestController("/person")
public class PersonController {

    @Autowired
    PersonMapper personMapper;

    @GetMapping("/list")
    public List<Person> userList() {
        return personMapper.selectList(null);
    }

}
