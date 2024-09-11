package com.bigid.textmatcher.model;

import lombok.Data;

import java.util.List;

@Data
public class Batch {
    private List<String> lines;
    private Offset startOffset;
    private Offset endOffset;
    private boolean isLast;

    public Batch(List<String> lines, boolean isLast) {
        this.lines = lines;
        this.isLast = isLast;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public void add(String line) {
        this.lines.add(line);
    }

    public int size() {
        return this.lines.size();
    }
}
