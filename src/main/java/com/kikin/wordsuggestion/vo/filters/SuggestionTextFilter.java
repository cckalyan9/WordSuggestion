package com.kikin.wordsuggestion.vo.filters;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.kikin.wordsuggestion.nlp.SuggestionGeneratorImpl;
import com.kikin.wordsuggestion.vo.Suggestion;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/10/12
 * Time: 12:07 PM
 * Predicate filter for suggestions.
 */
public class SuggestionTextFilter implements Predicate<Suggestion> {

    private Collection<String> texts = Lists.newArrayList();

    public SuggestionTextFilter(Collection<String> texts) {
        this.texts = texts;
    }


    @Override
    public boolean apply(@Nonnull Suggestion suggestion) {

        System.out.println("Testing");
        if (suggestion.getTerm().trim().split(SuggestionGeneratorImpl.SPACE).length == 0) {
            return false;
        }

        final boolean filter = !texts.contains(suggestion.getTerm().trim());
        return filter;
    }
}
