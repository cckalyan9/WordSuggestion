package com.kikin.wordsuggestion.vo;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/11/12
 * Time: 2:43 PM
 * Test case of suggestionTerm
 */
public class SuggestionTermTest {
    private static Logger logger = LoggerFactory.getLogger(SuggestionTermTest.class);

    @Test
    public void testCompareToFunction() {

        List<SuggestionTerm> suggestionTerms = Lists.newArrayList(new SuggestionTerm("one", 1), new SuggestionTerm(
                "three", 3), new SuggestionTerm(
                "three1", 3), new SuggestionTerm("two", 2), new SuggestionTerm("seven", 7), new SuggestionTerm("four",
                4));


        Collections.sort(suggestionTerms);

        for (SuggestionTerm suggestionTerm : suggestionTerms) {
            logger.info(suggestionTerm.toString());
        }

        assertTrue(suggestionTerms.get(0).getOffset() == 1);
        assertTrue(suggestionTerms.get(suggestionTerms.size() - 1).getOffset() == 7);


    }

}
