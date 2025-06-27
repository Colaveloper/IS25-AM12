package it.polimi.ingsw.galaxytruckers.shared.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

/**
 * Utility class providing helper methods for concurrency control using locks.
 * This class contains methods to execute code blocks with read or write locks,
 * ensuring proper lock acquisition and release regardless of execution outcome.
 */
public class LockUtils {
    /**
     * Executes a function that returns a value with either a read or write lock.
     * The lock is automatically released after the function completes, even if an exception occurs.
     *
     * @param <T> the type of the value returned by the function
     * @param readWriteLock the lock to use for synchronization
     * @param method the function to execute within the lock
     * @param write if true, uses a write lock; if false, uses a read lock
     * @return the value returned by the provided function
     */
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

    /**
     * Executes a runnable operation with either a read or write lock.
     * The lock is automatically released after the operation completes, even if an exception occurs.
     *
     * @param readWriteLock the lock to use for synchronization
     * @param method the operation to execute within the lock
     * @param write if true, uses a write lock; if false, uses a read lock
     */
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
