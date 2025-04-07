package threads;

import java.util.concurrent.*;

public class FutureTaskArraySum {
    public static void main(String[] args) throws Exception {
        int[] array = new int[10000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        // 创建两个任务
        FutureTask<Long> task1 = new FutureTask<>(new SumTask(array, 0, array.length / 2));
        FutureTask<Long> task2 = new FutureTask<>(new SumTask(array, array.length / 2, array.length));

        // 创建线程执行任务
        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        t1.start();
        t2.start();

        // 获取结果
        long sum1 = task1.get();
        long sum2 = task2.get();

        System.out.println(new StringBuilder().append("Total sum: ").append(sum1 + sum2));
    }

    static class SumTask implements Callable<Long> {
        private final int[] array;
        private final int start;
        private final int end;

        public SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
    }
}