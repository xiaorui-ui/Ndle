package test;

import java.util.function.Supplier;


public abstract class Test {
    public <U> void equals(U u, U v) {
        if (u.equals(v)) {
            System.out.println("true");
        } else {
            System.out.println("false" + u.toString() + v.toString());
        }
    }

    public <T> void test(Supplier<T> s) {
        long i = System.nanoTime();
        System.out.println(s.get());
        long j = System.nanoTime();
        System.out.println((j - i) * Math.pow(10, -9));
    }

    public abstract void run();
    
}
