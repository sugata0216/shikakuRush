package com.example.shikakurush.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class TopController {

    @GetMapping("/top")
    public String top() {
        return "user/top";
    }
}