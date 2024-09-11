package com.bigid.textmatcher.service;

import com.bigid.textmatcher.model.Batch;
import com.bigid.textmatcher.model.Offset;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public interface BatchHandler {
    Queue<Map<String, List<Offset>>> handle(BlockingQueue<Batch> batchesQueue);
}
