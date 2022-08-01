package com.xd.jdk.lock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("api")
public class ReentrantLockTest {
    /**
     * ReentrantLock默认情况下为不公平锁
     * 不公平锁与公平锁的区别：
     * 公平情况下，操作会排一个队按顺序执行，来保证执行顺序。（会消耗更多的时间来排队）
     * 不公平情况下，是无序状态允许插队，jvm会自动计算如何处理更快速来调度插队。（如果不关心顺序，这个速度会更快）
     */

    private ReentrantLock lock = new ReentrantLock(); //参数默认false，不公平锁
    //private ReentrantLock lock = new ReentrantLock(true); //公平锁

    /**
     * 场景1：如果发现该操作已经在执行中则不再执行（有状态执行）
     * a、用在定时任务时，如果任务执行时间可能超过下次计划执行时间，确保该有状态任务只有一个正在执行，忽略重复触发。
     * b、用在界面交互时点击执行较长时间请求操作时，防止多次点击导致后台重复执行（忽略重复触发）。
     * 以上两种情况多用于进行非重要任务防止重复执行，（如：清除无用临时文件，检查某些资源的可用性，数据备份操作等）
     * @throws InterruptedException
     */
    @GetMapping("tryLock")
    public void test() throws InterruptedException {
        boolean res = lock.tryLock();
        System.out.println(res);
        if (res) {  //如果已经被lock，则立即返回false不会等待，达到忽略操作的效果
            try {
                //操作
                Thread.sleep(4000);//使用延时操作模拟调用远程接口
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 场景2：如果发现该操作已经在执行，等待一个一个执行（同步执行，类似synchronized）
     * @throws InterruptedException
     */
    @GetMapping("tryLock2")
    public void test2() throws InterruptedException {
        try {
            lock.lock(); //如果被其它资源锁定，会在此等待锁释放，达到暂停的效果
            System.out.println(Thread.currentThread().getName() + " running");
            //操作
            Thread.sleep(4000);//使用延时操作模拟调用远程接口
            System.out.println("tryLock2");
        } finally {
            lock.unlock();
        }
    }

    /**
     * 场景3：如果发现该操作已经在执行，则尝试等待一段时间，等待超时则不执行（尝试等待执行）
     * 这种其实属于场景2的改进，等待获得锁的操作有一个时间的限制，如果超时则放弃执行。
     * 用来防止由于资源处理不当长时间占用导致死锁情况（大家都在等待资源，导致线程队列溢出）。
     * @throws InterruptedException
     */
    @GetMapping("tryLock3")
    public void test3() throws InterruptedException {
        try {
            if (lock.tryLock(2, TimeUnit.SECONDS)) {  //如果已经被lock，尝试等待5s，看是否可以获得锁，如果5s后仍然无法获得锁则返回false继续执行
                try {
                    //操作
                    Thread.sleep(4000);//使用延时操作模拟调用远程接口
                    System.out.println("tryLock3");
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace(); //当前线程被中断时(interrupt)，会抛InterruptedException
        }
    }

    /**
     * 场景4：如果发现该操作已经在执行，等待执行。这时可中断正在进行的操作立刻释放锁继续下一操作。
     * synchronized与Lock在默认情况下是不会响应中断(interrupt)操作，会继续执行完。lockInterruptibly()提供了可中断锁来解决此问题。
     * 这种情况主要用于取消某些操作对资源的占用。如：（取消正在同步运行的操作，来防止不正常操作长时间占用造成的阻塞）
     * @throws InterruptedException
     */
    @GetMapping("tryLock4")
    public void test4() throws InterruptedException {
        try {
            lock.lockInterruptibly();
            //操作

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
