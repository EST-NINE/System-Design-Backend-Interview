package data_struct;

import java.util.LinkedList;

public class MinStack {
    LinkedList<Integer> stack = new LinkedList<>();
    LinkedList<Integer> min = new LinkedList<>();

    public MinStack() {
        min.push(Integer.MAX_VALUE);
    }

    public void push(int val) {
        stack.push(val);
        min.push(Math.min(min.peek(), val));
    }

    public void pop() {
        if(stack.isEmpty()){
            return;
        }
        stack.pop();
        min.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return min.peek();
    }
}
