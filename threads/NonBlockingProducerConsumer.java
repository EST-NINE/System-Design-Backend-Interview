package threads;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NonBlockingProducerConsumer {
    // 定义一个非阻塞队列作为共享缓冲区
    private static final Queue<String> queue = new ConcurrentLinkedQueue<>();
    private static final int MAX_CAPACITY = 5; // 队列的最大容量

    // 定义锁和条件变量
    private static final Lock lock = new ReentrantLock();
    private static final Condition notFull = lock.newCondition(); // 队列未满
    private static final Condition notEmpty = lock.newCondition(); // 队列非空

    public static void main(String[] args) {
        // 创建厨子线程（生产者）
        Thread chefThread = new Thread(() -> {
            try {
                while (true) {
                    lock.lock(); // 获取锁
                    try {
                        // 如果队列已满，等待
                        while (queue.size() == MAX_CAPACITY) {
                            System.out.println("Queue is full, Chef is waiting...");
                            notFull.await();
                        }

                        // 生产菜品
                        String dish = "Dish-" + System.currentTimeMillis();
                        queue.add(dish);
                        System.out.println("Chef produced: " + dish);

                        // 唤醒消费者
                        notEmpty.signalAll();
                    } finally {
                        lock.unlock(); // 释放锁
                    }

                    // 模拟厨子生产时间
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 创建客人线程（消费者）
        Thread customerThread = new Thread(() -> {
            try {
                while (true) {
                    lock.lock(); // 获取锁
                    try {
                        // 如果队列为空，等待
                        while (queue.isEmpty()) {
                            System.out.println("Queue is empty, Customer is waiting...");
                            notEmpty.await();
                        }

                        // 消费菜品
                        String dish = queue.poll();
                        System.out.println("Customer consumed: " + dish);

                        // 唤醒生产者
                        notFull.signalAll();
                    } finally {
                        lock.unlock(); // 释放锁
                    }

                    // 模拟客人消费时间
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 启动线程
        chefThread.start();
        customerThread.start();
    }
}