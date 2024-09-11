package com.bigid.textmatcher.service;

import com.bigid.textmatcher.model.Batch;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface FileReader {
    void read(String source, BlockingQueue<Batch> targetBatchQueue);
}
