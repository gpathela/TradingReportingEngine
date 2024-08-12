package com.vanguard.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;



@RestController
@RequestMapping("")
public class MainController {


    @GetMapping("")
    public String index(HttpServletRequest request, Model model) { 
        return getResponseString(request);
    }

    @GetMapping("/api")
    public String apiurl(HttpServletRequest request, Model model) {
        return getResponseString(request);
    }


    private String getResponseString(HttpServletRequest request) {
        String hostedUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return String.format("Please visit <a href=\"/swagger.html\"> %s/swagger.html </a> for accessing API", hostedUrl);
    }


}
