package com.kikin.wordsuggestion.vo;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WordSuggestionInputBuilder {


    private String context;
    private String fullText;
    private TouchParameters touchParameters;

    private List<Rects> rectangles;


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

    public WordSuggestionInputBuilder setWords(List<Rects> rectsList) {
        this.rectangles = rectsList;
        return this;
    }


    // Main builder.
    public WordSuggestionInput createWordSuggestionInput() {
        List<String> wordsNearTouchPoint = getWordsNearTouchPoint(
                rectangles);

        if (touchParameters == null) {
            throw new IllegalArgumentException("Touch Parameter, touch location is mandatory.");
        }

        if (wordsNearTouchPoint == null || wordsNearTouchPoint.size() == 0) {
            throw new IllegalArgumentException("Rects suggestion input cannot be generated with empty text blocks.");
        }

        // Words near touch point is mandatory.
        return new WordSuggestionInput((wordsNearTouchPoint.get(0)), context, fullText, touchParameters,
                wordsNearTouchPoint);
    }


    private List<String> getWordsNearTouchPoint(List<Rects> rectsList) {


        // Populate distanceFromTouchPoint.
        for (Rects rects : rectsList) {
            rects.calculateDistanceFromTouchPoint(touchParameters.getTouchLocation());
        }

        Collections.sort(rectsList, new ProximityValueComparator());
        return Lists.newArrayList(Collections2.transform(rectsList, WORD_STRING_FUNCTION));
    }


    // Comparator and transform functions.

    private class ProximityValueComparator implements Comparator<Rects> {


        @Override
        public int compare(Rects o1, Rects o2) {
            double difference = o1.getDistanceFromTouchPoint() - o2.getDistanceFromTouchPoint();
            return (difference > 0) ? 1 : -1;
        }
    }

    private static final Function<Rects, String> WORD_STRING_FUNCTION = new Function<Rects, String>() {
        @Override
        public String apply(@Nullable Rects rects) {
            return rects.getWord();
        }
    };
}