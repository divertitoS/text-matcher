package com.bigid.textmatcher.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SyncManager {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void await() {
        lock.lock();

        try {
            condition.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("InterruptedException caught during SyncManager.await() operation.", e);
        } finally {
            lock.unlock();
        }
    }

    public void signalAll() {
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
