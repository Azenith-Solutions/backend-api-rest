package com.azenithsolutions.backendapirest.v1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tests")
public class TestController {
    @GetMapping
    public String test() {
        return "Hello World";
    }
}
