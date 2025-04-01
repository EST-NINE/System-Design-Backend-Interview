package data_struct;

import java.util.Arrays;

public class MyList {
    int[] ans = new int[10];
    int cur = 0;

    public void add(int val) {
        ensureCapacity();
        ans[cur++] = val;
    }

    public int[] getArray() {
        return Arrays.copyOf(ans, cur);
    }

    private void ensureCapacity() {
        if (cur == ans.length)
            ans = Arrays.copyOf(ans, cur * 2);
    }
}