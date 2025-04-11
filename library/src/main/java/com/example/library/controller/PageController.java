package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String indexPage() {
        return "index"; // loads index.html
    }

    /*
     * @GetMapping("/admin")
     * public String adminPage() {
     * return "admin"; // loads admin.html
     * }
     */

    @GetMapping("/user")
    public String userPage() {
        return "user"; // loads user.html
    }
}
