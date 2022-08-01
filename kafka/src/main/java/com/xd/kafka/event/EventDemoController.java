package com.xd.kafka.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventDemoController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String register(String userName) {
        userService.register(userName);
        return null;
        //throw new RuntimeException("异常");
    }

}
