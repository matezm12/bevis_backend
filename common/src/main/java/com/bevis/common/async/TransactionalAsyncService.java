package com.bevis.common.async;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class TransactionalAsyncService {

    private final AsyncService asyncService;

    /**
     * Run WITHOUT transactional wrapper
     * @param runnable
     */
    public void runAfterCommit(final Runnable runnable) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            public void afterCommit(){
                asyncService.run0(runnable);
            }
        });
    }

    /**
     * Run WITH transactional wrapper
     * @param runnable
     */
    public void runTxAfterCommit(final Runnable runnable) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            public void afterCommit(){
                asyncService.run(runnable);
            }
        });
    }
}
