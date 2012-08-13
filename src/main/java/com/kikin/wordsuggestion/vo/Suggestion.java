package com.kikin.wordsuggestion.vo;

import com.google.common.collect.Lists;
import com.kikin.wordsuggestion.utils.SuggestionScore;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/10/12
 * Time: 12:58 AM
 * Suggestion Object from the NLP Module.
 */
public class Suggestion implements Comparable<Suggestion> {

    private static final String PROPER_NOUN = "NNP";
    private static final String NO_VALUE = "O";
    private static final int NO_OFFSET = -1;

    private SuggestionTerm suggestionTerm;

    private List<SuggestionTerm> mergedSuggestions;


    //Part of Speech tagging.
    private final String posTag;
    //Named Entity extraction.
    private final String nerTag;


    private Suggestion(SuggestionTerm term, String posTag, String nerTag) {
        this.suggestionTerm = term;
        this.posTag = posTag;
        this.nerTag = nerTag;
        mergedSuggestions = Lists.newArrayList(suggestionTerm);

    }

    public static Suggestion createSuggestion(String term, String posTag, String nerTag, int offset) {
        return new Suggestion(new SuggestionTerm(term, offset), posTag, nerTag);
    }

    public static Suggestion createSuggestionForMerge(String term, String posTag, String nerTag, int offset,
                                                      List<SuggestionTerm> mergedSuggestions) {
        final Suggestion suggestion = new Suggestion(new SuggestionTerm(term, offset), posTag, nerTag);
        suggestion.mergedSuggestions = mergedSuggestions;
        return suggestion;
    }

    public static Suggestion createSuggestionWithJustTerm(String term) {
        return new Suggestion(new SuggestionTerm(term, NO_OFFSET), NO_VALUE, NO_VALUE);
    }


    public String getTerm() {

        // Greater than indicates merge performed on this object.
        if (mergedSuggestions.size() > 1) {

            Collections.sort(mergedSuggestions);
            StringBuilder termBuilder = new StringBuilder();
            for (SuggestionTerm mergedSuggestion : mergedSuggestions) {
                termBuilder.append(mergedSuggestion.getTerm()).append(" ");
            }
            return termBuilder.toString();
        }
        return suggestionTerm.term;
    }


    public String getPosTag() {
        return posTag;
    }

    public String getNerTag() {
        return nerTag;
    }

    public int getOffset() {
        return suggestionTerm.offset;
    }

    /**
     * @return
     */
    public boolean hasPosTag() {

        return (this.getPosTag().equals(NO_VALUE)) ? false : true;
    }

    public boolean hasNerTag() {

        return (this.getNerTag().equals(NO_VALUE)) ? false : true;
    }

    public boolean hasOffSetInformation() {

        return (this.getOffset() == NO_OFFSET) ? false : true;
    }


    /**
     * This method should be used to create compound words.
     *
     * @param other
     * @return - New Instance of suggestion and hence safe to manipulate outside the scope.Anyways Suggestion is immutable.
     */
    public Suggestion merge(Suggestion other) {

        //int newOffSet;
        String newNerTag;
        String newPosTag;
        /* if (this.suggestionTerm.offset < other.suggestionTerm.offset) {
            newOffSet = this.getOffset();
        } else {
            newOffSet = other.getOffset();
        }*/
        newNerTag = (this.getNerTag() == null) ? other.getNerTag() : this.getNerTag();
        // Pos tag is slightly tricky. Although both of them have to be noun constructs, Proper Nouns are rated high
        // than regular nouns.
        newPosTag = (this.getPosTag() != null && this.getPosTag().startsWith(PROPER_NOUN)) ? this.getPosTag() : other
                .getPosTag();

        final Suggestion suggestion = Suggestion.createSuggestionForMerge(this.getTerm(), newPosTag, newNerTag,
                this.getOffset(), mergedSuggestions);


        suggestion.mergedSuggestions.add(new SuggestionTerm(other.getTerm(), other.getOffset()));
        return suggestion;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Suggestion)) return false;

        Suggestion that = (Suggestion) o;

        if (suggestionTerm.offset != that.suggestionTerm.offset) return false;
        if (!suggestionTerm.term.equals(that.suggestionTerm.term)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = suggestionTerm.term.hashCode();
        result = 31 * result + suggestionTerm.offset;
        return result;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "term='" + getTerm() + '\'' +
                ", posTag='" + getPosTag() + '\'' +
                ", nerTag='" + getNerTag() + '\'' +
                ", offset=" + getOffset() +
                '}';
    }

    @Override
    public int compareTo(Suggestion suggestion) {
        return SuggestionScore.score(suggestion).compareTo(SuggestionScore.score(this));
    }
}
