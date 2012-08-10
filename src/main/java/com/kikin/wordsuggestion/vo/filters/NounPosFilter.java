package com.kikin.wordsuggestion.vo.filters;

import com.google.common.base.Predicate;

import javax.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/10/12
 * Time: 1:31 PM
 * Filters all but Nouns in POS
 */
public class NounPosFilter implements Predicate<String> {


    @Override
    public boolean apply(@Nonnull String s) {

        return (s.startsWith("NN")) ? true : false;
    }
}
