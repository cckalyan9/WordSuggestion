package com.kikin.wordsuggestion.vo;

public class SuggestionTerm implements Comparable<SuggestionTerm> {
    final String term;//Positional information.
    final int offset;

    public SuggestionTerm(String term, int offset) {
        this.term = term;
        this.offset = offset;
    }

    public String getTerm() {
        return term;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public int compareTo(SuggestionTerm suggestionTerm) {
        return this.offset - suggestionTerm.offset;
    }

    @Override
    public String toString() {
        return "SuggestionTerm{" +
                "term='" + term + '\'' +
                ", offset=" + offset +
                '}';
    }
}