package com.bigid.textmatcher.service.impl;

import com.bigid.textmatcher.exception.InvalidOffsetException;
import com.bigid.textmatcher.model.Offset;
import com.bigid.textmatcher.service.MatcherService;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bigid.textmatcher.util.Constants.WORDS;

@Service
public class MatcherServiceImpl implements MatcherService {

    @Override
    public Map<String, List<Offset>> findMatches(List<String> lines, Offset startOffset, Offset endOffset) {
        if (!Objects.nonNull(startOffset) || !Objects.nonNull(endOffset) ) {
            throw new InvalidOffsetException("Offset cannot be null");
        }

        Map<String, List<Offset>> wordToOffsetsMap = new HashMap<>();

        String patternString = String.join("|", WORDS);
        Pattern pattern = Pattern.compile(patternString);

        long startLineOffset = startOffset.getLineOffset();
        long startCharOffset = startOffset.getCharOffset();
        long endLineOffset = endOffset.getLineOffset();
        long currentLineOffset = startLineOffset;
        long currentCharOffset = startCharOffset;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (i + startLineOffset < startLineOffset || i + startLineOffset > endLineOffset) {
                continue;
            }

            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String foundName = matcher.group();
                int charOffsetInLine = matcher.start();
                Offset offset = new Offset(currentLineOffset, currentCharOffset + charOffsetInLine);
                wordToOffsetsMap.computeIfAbsent(foundName, k -> new LinkedList<>()).add(offset);
            }
            currentLineOffset++;
            currentCharOffset += line.length();
        }

        return wordToOffsetsMap;
    }
}
