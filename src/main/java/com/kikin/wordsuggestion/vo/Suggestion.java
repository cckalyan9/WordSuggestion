package com.kikin.wordsuggestion.vo;

import com.kikin.wordsuggestion.utils.SuggestionScore;

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
    private static final String NO_VALUE = "EMPTY";
    private static final int NO_OFFSET = -1;
    private final String term;

    //Part of Speech tagging.
    private final String posTag;

    //Named Entity extraction.
    private final String nerTag;


    //Positional information.
    private final int offset;


    private Suggestion(String term, String posTag, String nerTag, int offset) {
        this.term = term;
        this.posTag = posTag;
        this.nerTag = nerTag;
        this.offset = offset;
    }

    public static Suggestion createSuggestion(String term, String posTag, String nerTag, int offset) {
        return new Suggestion(term, posTag, nerTag, offset);
    }

    public static Suggestion createSuggestionWithJustTerm(String term) {
        return new Suggestion(term, NO_VALUE, NO_VALUE, NO_OFFSET);
    }


    public String getTerm() {
        return term;
    }

    public String getPosTag() {
        return posTag;
    }

    public String getNerTag() {
        return nerTag;
    }

    public int getOffset() {
        return offset;
    }

    /**
     * Usage of == equality check is conscious.
     *
     * @return
     */
    public boolean hasPosTag() {

        return (this.getPosTag() == NO_VALUE) ? false : true;
    }

    public boolean hasNerTag() {

        return (this.getNerTag() == NO_VALUE) ? false : true;
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

        int newOffSet;
        String newTerm;
        String newNerTag;
        String newPosTag;
        if (this.offset < other.offset) {

            newOffSet = this.getOffset();
            newTerm = this.getTerm() + " " + other.getTerm();
        } else {
            newOffSet = other.getOffset();
            newTerm = other.getTerm() + " " + this.getTerm();
        }


        newNerTag = (this.getNerTag() == null) ? other.getNerTag() : this.getNerTag();

        // Pos tag is slightly tricky. Although both of them have to be noun constructs, Proper Nouns are rated high
        // than regular nouns.
        newPosTag = (this.getPosTag() != null && this.getPosTag().startsWith(PROPER_NOUN)) ? this.getPosTag() : other
                .getPosTag();

        return new Suggestion(newTerm, newPosTag, newNerTag, newOffSet);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Suggestion)) return false;

        Suggestion that = (Suggestion) o;

        if (offset != that.offset) return false;
        if (!term.equals(that.term)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = term.hashCode();
        result = 31 * result + offset;
        return result;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "term='" + term + '\'' +
                ", posTag='" + posTag + '\'' +
                ", nerTag='" + nerTag + '\'' +
                ", offset=" + offset +
                '}';
    }

    @Override
    public int compareTo(Suggestion suggestion) {
        return SuggestionScore.score(suggestion).compareTo(SuggestionScore.score(this));
    }
}
