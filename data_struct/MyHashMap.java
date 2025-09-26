package data_struct;

import java.util.Objects;

public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;

    // 节点类
    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;
        int hash;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.capacity = DEFAULT_CAPACITY;
    }

    // 核心方法：put
    public V put(K key, V value) {
        return putVal(hash(key), key, value);
    }

    private V putVal(int hash, K key, V value) {
        if (table == null || table.length == 0) {
            resize();
        }

        int n = table.length;
        int index = (n - 1) & hash; // 计算索引位置

        // 检查是否已存在相同key
        Node<K, V> p = table[index];
        while (p != null) {
            if (p.hash == hash && (Objects.equals(key, p.key))) {
                V oldValue = p.value;
                p.value = value;
                return oldValue;
            }
            p = p.next;
        }

        // 插入新节点（头插法）
        table[index] = new Node<>(hash, key, value, table[index]);
        size++;

        if (size > capacity) {
            resize();
        }

        return null;
    }

    // 核心方法：get
    public V get(K key) {
        Node<K, V> e = getNode(hash(key), key);
        return e == null ? null : e.value;
    }

    private Node<K, V> getNode(int hash, K key) {
        if (table == null || table.length == 0) return null;

        int index = (table.length - 1) & hash;
        Node<K, V> p = table[index];

        while (p != null) {
            if (p.hash == hash && (Objects.equals(key, p.key))) {
                return p;
            }
            p = p.next;
        }
        return null;
    }

    // 扩容方法
    private void resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int newCap = oldCap == 0 ? capacity : oldCap << 1; // 扩容2倍

        @SuppressWarnings("unchecked")
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        capacity = (int)(newCap * loadFactor);

        if (oldTab != null) {
            for (int i = 0; i < oldCap; i++) {
                Node<K, V> e = oldTab[i];
                while (e != null) {
                    Node<K, V> next = e.next;
                    int index = (newCap - 1) & e.hash;
                    e.next = newTab[index]; // 头插法
                    newTab[index] = e;
                    e = next;
                }
            }
        }
    }

    // 哈希函数
    private int hash(K key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16); // 扰动函数，减少哈希冲突
    }

    public int size() {
        return size;
    }
}