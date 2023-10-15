package com.doksanbir.ratelimiter.algorithm;

import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.model.UserRole;
import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Comparator;

@Slf4j
public class PriorityQueueRateLimiting implements RateLimitAlgorithm {

    private final PriorityBlockingQueue<User> userQueue;
    private final int maxQueueSize;
    private final Lock lock;
    private final Condition condition;

    public PriorityQueueRateLimiting(int maxQueueSize) {
        this.userQueue = new PriorityBlockingQueue<>(maxQueueSize, Comparator.comparingInt(this::getPriority));
        this.maxQueueSize = maxQueueSize;
        this.lock = new ReentrantLock(true); // Make the lock fair
        this.condition = lock.newCondition();
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(UserRole.ADMIN));
    }

    private int getPriority(User user) {
        return isAdmin(user) ? 1 : 2;
    }

    @Override
    public boolean shouldLimitRequest(User user) {
        return !tryConsume(user);
    }

    @Override
    public boolean tryConsume(User user) {
        boolean acquired = false;
        try {
            acquired = lock.tryLock();
            if (!acquired) {
                return false; // Lock was not acquired; another thread is in this method
            }

            if (userQueue.size() >= maxQueueSize) {
                log.warn("Rate limiting user {} as the queue is full.", user.getUsername());
                return false;
            }

            userQueue.offer(user);
            log.info("User {} added to priority queue.", user.getUsername());
            return true;

        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }

    public void processRequest() {
        lock.lock();
        try {
            while (userQueue.isEmpty()) {
                condition.await();
            }

            User user = userQueue.poll();
            if (user != null) {
                // Simulate request processing here
                log.info("Processed request for user {}", user.getUsername());
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for user in queue.", e);
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.PRIORITY_QUEUE;
    }
}
