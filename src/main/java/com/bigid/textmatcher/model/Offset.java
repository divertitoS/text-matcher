package com.bigid.textmatcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Offset {
    private long lineOffset;
    private long charOffset;

    @Override
    public String toString() {
        return String.format("[lineOffset=%d, charOffset=%d]", lineOffset, charOffset);
    }
}
