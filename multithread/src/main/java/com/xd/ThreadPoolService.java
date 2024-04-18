package com.xd;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
public class ThreadPoolService {
    @Async("taskExecutor")
    public void F1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        for(int i = 0 ; i < 10 ; i++){
            System.out.println("A-"+i);
            Thread.sleep(500);
        }

    }

    @Async("taskExecutor")
    public void F2() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        for(int i = 20 ; i < 30 ; i++){
            System.out.println("B-"+i);
            Thread.sleep(500);
        }

    }
}
