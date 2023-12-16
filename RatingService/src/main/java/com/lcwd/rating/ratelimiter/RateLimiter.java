package com.lcwd.rating.ratelimiter;

import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
public class RateLimiter {
    private final Semaphore semaphore;

    public RateLimiter(){
        this.semaphore = new Semaphore(5);
        //Allow 5 requests at a time
    }

    public boolean tryAcquire(){
        return semaphore.tryAcquire();
    }

    public void release(){
        semaphore.release();
    }

}
