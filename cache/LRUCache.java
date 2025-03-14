package cache;

import java.util.HashMap;
import java.util.Map;

class LRUCache {
   private static class Node {
       int key, value;
       Node prev, next;

       Node(int key, int value) {
           this.key = key;
           this.value = value;
       }
   }

   private final int capacity;
   private final Node dummy = new Node(0, 0);
   private final Map<Integer, Node> keyToNode = new HashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        dummy.prev = dummy.next = dummy;
    }

    public int get(int key) {
        Node node = getNode(key); // getNode 会把对应节点移到链表头部
        return node == null ? -1 : node.value;
    }

    public void put(int key, int value) {
        Node node = getNode(key); // getNode 会把对应节点移到链表头部
        if(node != null) { // 如果已经存在则更新值
            node.value = value;
            return;
        }
        node = new Node(key, value);
        keyToNode.put(key, node);
        pushFront(node); // 把新节点放到链表头部
        if(keyToNode.size() > capacity) {
            Node backNode = dummy.prev;
            keyToNode.remove(backNode.key); // 移除链表尾部节点
            remove(backNode);
        }
    }

    private Node getNode(int key) {
        if(!keyToNode.containsKey(key)) {
            return null;
        }

        Node node = keyToNode.get(key);
        remove(node);
        pushFront(node);
        return node;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void pushFront(Node node) {
        node.prev = dummy;
        node.next = dummy.next;
        dummy.next.prev = node;
        dummy.next = node;
    }
}