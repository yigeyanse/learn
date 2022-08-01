package com.xd.jdk.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableTest {

    public static void main(String[] args) throws Exception {
        new CallableTest().threadPoolTest();
    }

    /**
     * 在所有操作结束后调用get()方法等待结果返回即可
     *
     * 因为Callable线程只是在调用get()方法后阻塞线程等待返回，那么如果这个时候主线程仍有一些费时操作（暂时不需要线程结果的）,
     * 直接执行即可，在所有操作结束后调用get()方法等待返回即可！
     */
    private void callTest() throws Exception {
        long start = System.currentTimeMillis();
        Callable<String> callable = ()->{
            System.err.println("远程费时接口开始调用......");
            Thread.sleep(5000);//使用延时操作模拟调用远程接口
            System.err.println("远程费时接口调用结束.....");
            return "你好";
        };
        FutureTask<String> futureTask = new FutureTask<>(callable);
        Thread thread = new Thread(futureTask);
        thread.start();
        System.err.println("主线程的剩余操作开始.....");
        Thread.sleep(2000);
        System.err.println("主线程的剩余操作结束.....");
        String result = futureTask.get();
        long time = (System.currentTimeMillis() - start)/1000;
        System.err.println("线程已结束，返回值：" + result + "，用时：" + time);
    }

    /**
     * 配合线程池同时执行多个费时操作
     *
     * 如果在一个Controller中需要调用多个远程接口，就可以使用此方法配合线程池使用，Callable接口线程主要应对的就是这种情况！
     * @throws InterruptedException
     */
    private void threadPoolTest() throws Exception {
        long start = System.currentTimeMillis();
        Callable<String> c1 = ()->{
            Thread.sleep(5000);//使用延时操作模拟调用远程接口
            System.err.println("接口1调用结束.....");
            return "返回值1";
        };
        Callable<String> c2 = ()->{
            Thread.sleep(1000);//使用延时操作模拟调用远程接口
            System.err.println("接口2调用结束.....");
            return "返回值2";
        };
        Callable<String> c3 = ()->{
            Thread.sleep(4000);//使用延时操作模拟调用远程接口
            System.err.println("接口3调用结束.....");
            return "返回值3";
        };
        Callable<String> c4 = ()->{
            Thread.sleep(3000);//使用延时操作模拟调用远程接口
            System.err.println("接口4调用结束.....");
            return "返回值4";
        };
        Callable<String> c5 = ()->{
            Thread.sleep(2000);//使用延时操作模拟调用远程接口
            System.err.println("接口5调用结束.....");
            return "返回值5";
        };
        List<Callable<String>> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        list.add(c5);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<String>> results =  executor.invokeAll(list);
        executor.shutdownNow();
        results.forEach((res)->{
            try {
                System.out.println(res.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        long time = (System.currentTimeMillis() - start)/1000;
        System.err.println("操作结束，用时：" + time);
    }
}
