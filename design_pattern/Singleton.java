package design_pattern;

// 单例模式-懒汉式-双重校验锁
public class Singleton {

    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

// 单例模式-懒汉式
class Singleton2 {
    private static Singleton2 instance;

    private Singleton2() {}

    public static Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}


// 单例模式-饿汉式
class Singleton3 {
    private static Singleton3 instance = new Singleton3();
    
    private Singleton3() {}
    
    public static Singleton3 getInstance() {
        return instance;
    }
}