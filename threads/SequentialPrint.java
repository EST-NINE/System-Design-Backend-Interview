package threads;

public class SequentialPrint {
    private static final Object lock = new Object();
    private static int state = 0;

    public static void main(String[] args) {
        Thread a = new Thread(() -> print("A", 0));
        Thread b = new Thread(() -> print("B", 1));
        Thread c = new Thread(() -> print("C", 2));

        a.start();
        b.start();
        c.start();
    }

    private static void print(String letter, int targetState) {
        for (int i = 0; i < 10; i++) {
            synchronized (lock) {
                while (state % 3 != targetState) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(letter);
                state++;
                lock.notifyAll();
            }
        }
    }
}