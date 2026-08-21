package com.goldencrown.takeaway_backend;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController{
    @GetMapping("/api/hello")
    public String hello(){
        return "Golden Crown takeaway backend is alive";
    }
}