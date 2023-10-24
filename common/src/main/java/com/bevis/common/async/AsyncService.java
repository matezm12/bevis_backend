package com.bevis.common.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncService {

    /**
     * Run WITH transactional wrapper
     * @param runnable
     */
    @Transactional
    @Async
    public void run(final Runnable runnable) {
        runnable.run();
    }

    /**
     * Run WITHOUT transactional wrapper
     * @param runnable
     */
    @Async
    public void run0(final Runnable runnable) {
        runnable.run();
    }
}
