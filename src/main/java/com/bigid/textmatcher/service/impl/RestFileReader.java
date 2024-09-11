package com.bigid.textmatcher.service.impl;

import com.bigid.textmatcher.model.Batch;
import com.bigid.textmatcher.model.Offset;
import com.bigid.textmatcher.service.FileReader;
import com.bigid.textmatcher.util.SyncManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import static com.bigid.textmatcher.util.Constants.BATCH_SIZE;
import static com.bigid.textmatcher.util.Constants.INITIAL_OFFSET_END;
import static com.bigid.textmatcher.util.Constants.INITIAL_OFFSET_START;

@Slf4j
@Component
@AllArgsConstructor
public class RestFileReader implements FileReader {
    private final RestTemplate restTemplate;
    private final SyncManager syncManager;

    @Override
    public void read(String sourceUri, BlockingQueue<Batch> targetBatchQueue) {
        restTemplate.execute(sourceUri, HttpMethod.GET, null, response -> {
            BufferedReader bufferedBodyReader = getBufferedBodyReader(response);

            Batch batch = new Batch(new LinkedList<>(), false);
            String line;
            long startLineOffset = INITIAL_OFFSET_START;
            long endLineOffset = INITIAL_OFFSET_END;
            long startCharOffset = INITIAL_OFFSET_START;
            long endCharOffset = INITIAL_OFFSET_END;

            while ((line = bufferedBodyReader.readLine()) != null) {
                batch.add(line);
                endLineOffset++;
                endCharOffset += line.length();

                if (batch.size() >= BATCH_SIZE) {
                    batch.setStartOffset(new Offset(startLineOffset, startCharOffset));
                    batch.setEndOffset(new Offset(endLineOffset, endCharOffset));

                    putBatchToQueue(batch, targetBatchQueue);

                    batch = new Batch(new LinkedList<>(), false);
                    startLineOffset = endLineOffset;
                    startCharOffset = endCharOffset;
                }
            }

            batch.setLast(true);
            batch.setStartOffset(new Offset(startLineOffset, startCharOffset));
            batch.setEndOffset(new Offset(endLineOffset, endCharOffset));
            putBatchToQueue(batch, targetBatchQueue);

            log.info("Finished reading and waking up the BatchHandler thread");
            syncManager.signalAll();
            log.info("Waiting for the BatchHandler to finish processing all the batches that remain");
            syncManager.await();

            return true;
        });
    }

    private BufferedReader getBufferedBodyReader(ClientHttpResponse response) {
        InputStream inputStreamBody;

        try {
            inputStreamBody = response.getBody();
        } catch (IOException e) {
            throw new RuntimeException("Can't get inputStream body from response.", e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStreamBody);
        return new BufferedReader(inputStreamReader);
    }

    private void putBatchToQueue(Batch batch, BlockingQueue<Batch> targetBatchQueue) {
        try {
            log.info("Putting batch to blocking queue");
            targetBatchQueue.put(batch);
            log.info("Batch sent to blocking queue");
        } catch (InterruptedException e) {
            throw new RuntimeException("Something goes wrong during putting batch to blocking queue.", e);
        }
    }
}
