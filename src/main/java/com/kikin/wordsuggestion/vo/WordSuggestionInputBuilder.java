package com.kikin.wordsuggestion.vo;

import java.util.List;

public class WordSuggestionInputBuilder {
    private String selectedText;
    private String context;
    private String fullText;
    private TouchParameters touchParameters;
    private List<String> textsNearSelectedItem;

    public WordSuggestionInputBuilder setSelectedText(String selectedText) {
        this.selectedText = selectedText;
        return this;
    }

    public WordSuggestionInputBuilder setContext(String context) {
        this.context = context;
        return this;
    }

    public WordSuggestionInputBuilder setFullText(String fullText) {
        this.fullText = fullText;
        return this;
    }

    public WordSuggestionInputBuilder setTouchParameters(TouchParameters touchParameters) {
        this.touchParameters = touchParameters;
        return this;
    }

    public WordSuggestionInputBuilder setTextsNearSelectedItem(List<String> textsNearSelectedItem) {
        this.textsNearSelectedItem = textsNearSelectedItem;
        return this;
    }

    public WordSuggestionInput createWordSuggestionInput() {
        return new WordSuggestionInput(selectedText, context, fullText, touchParameters, textsNearSelectedItem);
    }
}