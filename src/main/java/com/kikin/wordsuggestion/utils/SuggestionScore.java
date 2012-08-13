package com.kikin.wordsuggestion.utils;

import com.kikin.wordsuggestion.vo.Suggestion;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/10/12
 * Time: 11:19 AM
 * Used in sorting suggestion, based on the information extracted by the NLP module. This score does not reflect
 * the proximity of the term with the touch location, this is the responsibility of the caller to incorporate that
 * information.
 */
public class SuggestionScore {


    public static final double posScore = 1.5;
    public static final double nerScore = 4;

    /**
     * Score is a function of term length and the availability of Ner and Pos tags.
     * <p/>
     * Lengthy terms with Pos and Ner tags will be  scored higher. Terms with Pos and Ner tags
     * will be scored higher that longer terms with either one or neither both.
     *
     * @param suggestion
     * @return
     */
    public static Double score(final Suggestion suggestion) {

        checkNotNull(suggestion);
        // Need to reduce weight of Length. Try Logarithmic or provide score based on discrete intervals, with higher values
        // incrementing by diminishing values.
        double score = Math.log(suggestion.getTerm().length());
        if (suggestion.hasPosTag()) {
            score *= posScore;
        }
        if (suggestion.hasNerTag()) {
            score *= nerScore;
        }
        return score;
    }


}
