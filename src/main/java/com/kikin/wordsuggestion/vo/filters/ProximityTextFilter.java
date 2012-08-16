package com.kikin.wordsuggestion.vo.filters;

import com.google.common.base.Predicate;
import com.kikin.wordsuggestion.utils.StringConstants;
import com.kikin.wordsuggestion.vo.Suggestion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/10/12
 * Time: 3:53 PM
 * ${END}$
 */
public class ProximityTextFilter implements Predicate<Suggestion> {

    private final String text;


    public ProximityTextFilter(@Nonnull String text) {
        checkNotNull(text);
        this.text = text;
    }


    @Override
    public boolean apply(@Nullable Suggestion suggestion) {
        String[] split = suggestion.getTerm().toLowerCase().split(StringConstants.SPACE);
        for (String s : split) {
            if (s.equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }
}
