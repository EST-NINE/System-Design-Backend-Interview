package threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SequentialPrintWithLock {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition a = lock.newCondition();
    private static Condition b = lock.newCondition();
    private static Condition c = lock.newCondition();
    private static int state = 0;

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> print("A", 0, a, b));
        Thread threadB = new Thread(() -> print("B", 1, b, c));
        Thread threadC = new Thread(() -> print("C", 2, c, a));

        threadA.start();
        threadB.start();
        threadC.start();
    }

    private static void print(String letter, int targetState, Condition current, Condition next) {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                while (state % 3 != targetState) {
                    current.await();
                }
                System.out.print(letter);
                state++;
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}