package com.bpa.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pk")
public class indexController {
    @RequestMapping("/")
    public String index(){
        return "hello world";
    }
}
