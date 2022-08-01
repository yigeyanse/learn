package com.xd.jdk.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestInterrupt {

    public static void main(String[] args) {
        Thread t1 = new Thread(new RunTt3());
        Thread t2 = new Thread(new RunTt3());

        t1.start();
        t2.start();
        t2.interrupt();
    }

    static class RunTt3 implements Runnable {

        private static final Lock lock = new ReentrantLock();

        @Override
        public void run() {
            try {
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + " running");
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + " finished");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted");
            } finally {
                lock.unlock();
            }
        }
    }
}
