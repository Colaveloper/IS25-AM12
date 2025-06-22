package it.polimi.ingsw.galaxytruckers.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

public class LockUtils {
    public static <T> T withLock(ReadWriteLock readWriteLock, Supplier<T> method, boolean write) {
        Lock lock;
        if (write) {
            lock = readWriteLock.writeLock();
        } else {
            lock = readWriteLock.readLock();
        }
        lock.lock();
        try {
            return method.get();
        } finally {
            lock.unlock();
        }
    }

    public static void withLock(ReadWriteLock readWriteLock, Runnable method, boolean write) {
        Lock lock;
        if (write) {
            lock = readWriteLock.writeLock();
        } else {
            lock = readWriteLock.readLock();
        }
        lock.lock();
        try {
            method.run();
        } finally {
            lock.unlock();
        }
    }
}
