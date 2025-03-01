package com.example.articleManagement;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    @Test
    public void testThreadLocalSetAndGet() {
        ThreadLocal threadLocal =  new ThreadLocal<>();

        new Thread(() -> {
            threadLocal.set("Alice");
            System.out.println(Thread.currentThread().getName() + threadLocal.get());
        },"A").start();


        new Thread(() -> {
            threadLocal.set("Peter");
            System.out.println(Thread.currentThread().getName() + threadLocal.get());
        },"B").start();
    }
}
