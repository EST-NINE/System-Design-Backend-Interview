package cache;

import java.util.HashMap;
import java.util.Map;

class LRUCacheWithTTL {
    static class Node {
        int key, value;
        long expire; // 过期时间戳（ms），0 表示永不过期
        Node prev, next;

        public Node(int key, int value, long expire) {
            this.key = key;
            this.value = value;
            this.expire = expire;
        }

        public boolean isExpired() {
            return expire != 0 && System.currentTimeMillis() > expire;
        }
    }

    private final int capacity; // 缓存最大容量
    private final Node dummy = new Node(0, 0, 0); // 双向链表哨兵节点
    private final Map<Integer, Node> keyToNode = new HashMap<>(); // 键到节点的映射

    public LRUCacheWithTTL(int capacity) {
        this.capacity = capacity;
        dummy.prev = dummy;
        dummy.next = dummy;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void pushFront(Node node) {
        node.prev = dummy;
        node.next = dummy.next;
        node.prev.next = node;
        node.next.prev = node;
    }

    // 内部获取节点：先检查是否过期，未过期则移到头部（更新 LRU 顺序）
    private Node getNode(int key) {
        Node node = keyToNode.get(key);
        if (node == null) {
            return null;
        }
        // 检查是否过期，过期则移除并返回 null
        if (node.isExpired()) {
            remove(node);
            keyToNode.remove(key);
            return null;
        }
        // 未过期：移到头部（标记为最近使用）
        remove(node);
        pushFront(node);
        return node;
    }

    // 对外接口：获取缓存值（未命中或过期返回 -1）
    public int get(int key) {
        Node node = getNode(key);
        return node == null ? -1 : node.value;
    }

    // 对外接口：添加/更新缓存（带 TTL 过期时间，单位：毫秒）
    public void put(int key, int value, long ttl) {
        // 计算过期时间戳（ttl=0 表示永不过期）
        long expire = ttl <= 0 ? 0 : System.currentTimeMillis() + ttl;

        Node node = getNode(key);
        if (node != null) {
            node.value = value;
            node.expire = expire;
            return;
        }

        // 新节点：插入头部
        node = new Node(key, value, expire);
        keyToNode.put(key, node);
        pushFront(node);

        // 超过容量：移除最久未使用的节点（链表尾部）
        if (keyToNode.size() > capacity) {
            Node last = dummy.prev;
            remove(last);
            keyToNode.remove(last.key);
        }
    }

    // 清理所有过期节点（定时任务调用）
    private void cleanExpiredNodes() {
        // 遍历链表，移除所有过期节点
        Node cur = dummy.next;
        while (cur != dummy) {
            Node next = cur.next; // 先记录下一个节点，避免移除后指针丢失
            if (cur.isExpired()) {
                remove(cur);
                keyToNode.remove(cur.key);
            }
            cur = next;
        }
    }
}
