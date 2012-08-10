package com.kikin.wordsuggestion.vo;

import com.google.common.base.Strings;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 9:49 AM
 * Simple POJO representing the input to the word suggestion engine.
 */
public class WordSuggestionInput {


    private final String selectedText;

    private final String context;

    private final String fullText;

    private final TouchParameters touchParameters;

    private List<String> textsNearSelectedItem;


    protected WordSuggestionInput(String selectedText, String context, String fullText, TouchParameters touchParameters,
                                  List<String> textsNearSelectedItem) {

        checkArgument(!Strings.isNullOrEmpty(selectedText));
        checkArgument(!Strings.isNullOrEmpty(context));
        //checkArgument(!Strings.isNullOrEmpty(fullText));
        this.selectedText = selectedText;
        this.context = context;
        this.fullText = fullText;
        this.touchParameters = touchParameters;
        this.textsNearSelectedItem = textsNearSelectedItem;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public String getContext() {
        return context;
    }

    public String getFullText() {
        return fullText;
    }

    public TouchParameters getTouchParameters() {
        return touchParameters;
    }

    public List<String> getTextsNearSelectedItem() {
        return textsNearSelectedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordSuggestionInput that = (WordSuggestionInput) o;

        if (context != null ? !context.equals(that.context) : that.context != null) return false;
        if (selectedText != null ? !selectedText.equals(that.selectedText) : that.selectedText != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = selectedText != null ? selectedText.hashCode() : 0;
        result = 31 * result + (context != null ? context.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WordSuggestionInput{" +
                "selectedText='" + selectedText + '\'' +
                ", context='" + context + '\'' +
                ", fullText='" + fullText + '\'' +
                ", touchParameters=" + touchParameters +
                '}';
    }


}
