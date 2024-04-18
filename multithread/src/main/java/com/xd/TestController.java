package com.xd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thread")
public class TestController {

    @Autowired
    public ThreadPoolService threadPoolService;

    @GetMapping("/pool")
    public String pool() throws InterruptedException {
        threadPoolService.F1();
        threadPoolService.F2();
        return "332";
    }
}
