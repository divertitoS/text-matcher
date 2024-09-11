package com.bigid.textmatcher.service.impl;

import com.bigid.textmatcher.model.Offset;
import com.bigid.textmatcher.service.AggregatorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
public class AggregatorServiceImpl implements AggregatorService {

    @Override
    public Map<String, List<Offset>> aggregate(Queue<Map<String, List<Offset>>> matches) {
        Map<String, List<Offset>> aggregatedMatches = matches.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existingList, newList) -> {
                            existingList.addAll(newList);
                            return existingList;
                        }
                ));

        aggregatedMatches.forEach((word, offsets) -> {
            String offsetsString = offsets.toString();
            System.out.println(word + " --> " + offsetsString);
        });

        return aggregatedMatches;
    }
}
