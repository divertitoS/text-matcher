package com.bigid.textmatcher.service.impl;

import com.bigid.textmatcher.model.Batch;
import com.bigid.textmatcher.model.Offset;
import com.bigid.textmatcher.service.BatchHandler;
import com.bigid.textmatcher.service.MatcherService;
import com.bigid.textmatcher.util.SyncManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.bigid.textmatcher.util.Constants.CONCURRENT_MATCHERS_NUMBER;

@Slf4j
@Component
@AllArgsConstructor
public class ConcurrentBatchHandler implements BatchHandler {
    private final SyncManager syncManager;
    private final MatcherService matcherService;

    @Override
    public Queue<Map<String, List<Offset>>> handle(BlockingQueue<Batch> batchesQueue) {
        Queue<Map<String, List<Offset>>> matches = new ConcurrentLinkedQueue<>();
        Queue<Thread> threadsToShutdown = new ConcurrentLinkedQueue<>();
        AtomicBoolean running = new AtomicBoolean(true);

        ExecutorService matchersExecutorService = Executors.newFixedThreadPool(CONCURRENT_MATCHERS_NUMBER);
        Executors.newSingleThreadExecutor().submit(() -> {
            log.info("Submitting {} threads to handle batches concurrently", CONCURRENT_MATCHERS_NUMBER);

            for (int i = 0; i < CONCURRENT_MATCHERS_NUMBER; i++) {
                matchersExecutorService.submit(() -> {
                    threadsToShutdown.add(Thread.currentThread());

                    while (running.get()) {
                        Batch batchToProcess = takeBatchFromQueue(batchesQueue);
                        running.set(!batchToProcess.isLast());

                        log.info("Sending batch to matcher");
                        try {
                            matches.add(matcherService.findMatches(
                                            batchToProcess.getLines(),
                                            batchToProcess.getStartOffset(),
                                            batchToProcess.getEndOffset()
                                    )
                            );
                        } catch (Exception ex) {
                            log.error("Exception while processing batch: {}", ex.getMessage());
                            log.error("Batch offset: {}", batchToProcess.getStartOffset());
                        }
                        log.info("Batch has been processed by matcher successfully");
                    }
                });
            }

            log.info("Waiting for the FileReader to finish");
            syncManager.await();
            waitLastBatchHandling(threadsToShutdown);
            matchersExecutorService.shutdownNow();

            log.info("Waking up the FileReader thread and interrupting");
            syncManager.signalAll();
            Thread.currentThread().interrupt();
        });

        return matches;
    }

    private void waitLastBatchHandling(Queue<Thread> threadsToShutdown) {
        log.info("Waiting for a graceful finish to all matches");

        for (Thread thread : threadsToShutdown) {
            while (!Thread.State.WAITING.equals(thread.getState())) {
                // wait to finish gracefully all matchers
            }
        }
    }

    private Batch takeBatchFromQueue(BlockingQueue<Batch> batchesQueue) {
        Batch batch = null;

        try {
            log.info("Trying to retrieve batch from blocking queue");
            batch = batchesQueue.take();
            log.info("Batch retrieved!");
        } catch (InterruptedException e) {
            log.info("Forcefully shutting down thread in WAIT status.");
            Thread.currentThread().interrupt();
        }

        return batch;
    }
}
