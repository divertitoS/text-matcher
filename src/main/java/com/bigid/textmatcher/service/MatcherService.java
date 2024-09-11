package com.bigid.textmatcher.service;

import com.bigid.textmatcher.model.Offset;

import java.util.List;
import java.util.Map;

public interface MatcherService {
    Map<String, List<Offset>> findMatches(List<String> lines, Offset startOffset, Offset endOffset);
}
