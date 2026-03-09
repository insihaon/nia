package com.nia.korenrca.test.rabbitmq;

public class CacheEntry {
    private String text;
    private int sequenceNr;

    public CacheEntry(String text, int sequenceNr) {
        this.text = text;
        this.sequenceNr = sequenceNr;
    }

    public String getText() {
        return text;
    }

    public int getSequenceNr() {
        return sequenceNr;
    }

    // ... hashCode and equals
}
