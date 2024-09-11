package com.bigid.textmatcher.service;

import com.bigid.textmatcher.model.Offset;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface AggregatorService {
    Map<String, List<Offset>> aggregate(Queue<Map<String, List<Offset>>> matches);
}
