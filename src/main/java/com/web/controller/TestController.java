package com.web.controller;

import com.web.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/")
public class TestController {
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String userList(Model model) throws Exception {
        model.addAttribute("msg", "用户列表");
        model.addAttribute("hello","Hello, Spring Boot!");


        return "test";
    }

}
