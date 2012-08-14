package com.kikin.wordsuggestion.nlp;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kikin.wordsuggestion.utils.SuggestionScore;
import com.kikin.wordsuggestion.utils.TestVariables;
import com.kikin.wordsuggestion.vo.Suggestion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/10/12
 * Time: 11:34 AM
 * ${END}$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/wordhighlighter-test.xml"})

public class SuggestionGeneratorImplTest {

    private static Logger logger = LoggerFactory.getLogger(SuggestionGeneratorImplTest.class);
    private static final String SUGGESTION_DELIMITER = "-->";

    private static final String SUGGESTION_EXPECTATION_DELIMITER = "&&";


    @Autowired
    private SuggestionGenerator suggestionGenerator;

    @Autowired
    private TestVariables testVariables;


    private Map<String, List<String>> suggestionMap = Maps.newHashMap();
    //TODO: Replace concrete implementation with Stub. The Context returned by Standford NLP is very hard to Mock.
    @Qualifier("nlpModuleWithAllProcesses")
    @Autowired
    private KikinNLPModule kikinNLPModule;


    @Before
    public void loadSuggestionsFromPropertyFile() {

        if (testVariables.getSuggestions() != null) {
            final String[] split = testVariables.getSuggestions().split(SUGGESTION_DELIMITER);

            for (String individualSuggestion : split) {
                if (individualSuggestion != null) {
                    final String[] split1 = individualSuggestion.split(SUGGESTION_EXPECTATION_DELIMITER);
                    String key = null;
                    for (int j = 0; j < split1.length; j++) {
                        if (j == 0) {
                            key = split1[j];
                            suggestionMap.put(key, Lists.<String>newArrayList());
                        } else {
                            // NO need to perform explicit testing. Key will have to be initialized before this block is reached.
                            suggestionMap.get(key).add(split1[j]);
                        }

                    }
                }
            }
        }

    }


    @Test
    public void testExtractSuggestion() throws Exception {


        for (String inputText : suggestionMap.keySet()) {
            final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                    kikinNLPModule.processText(
                            inputText));

            logger.info("For term {} \n, we found the following suggestion {}", inputText, Joiner.on("\n").join(
                    suggestions));

            // We need to assert that the expected suggestion is part of the top 5 suggestions.
            assertTrue(doesSuggestionContainExpectedStrings(suggestions, suggestionMap.get(inputText)));

        }


    }

    private static boolean doesSuggestionContainExpectedStrings(List<Suggestion> suggestions, List<String> strings) {
        logger.info("Entering doesSuggestionContainExpectedStrings");

        if (suggestions == null || suggestions.size() == 0) {
            return false;
        }
        final List<Suggestion> partitionedSuggestion = Lists.partition(suggestions, 5).get(0);
        for (String expectedSuggestion : strings) {
            boolean valid = false;
            for (Suggestion suggestion : partitionedSuggestion) {


                final String trimmedSuggestion = CharMatcher.WHITESPACE.trimFrom(suggestion.getTerm());
                final String trimmedExpectedSuggestion = CharMatcher.WHITESPACE.trimFrom(
                        expectedSuggestion);
                if (trimmedSuggestion.equalsIgnoreCase(trimmedExpectedSuggestion)) {

                    valid = true;
                    break;

                }
            }
            if (!valid)
                return false;
        }

        logger.info("Exiting doesSuggestionContainExpectedStrings");
        return true;
    }


    @Test
    public void testExtractSuggestion7() throws Exception {


        List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText(
                        "Kate is one of Britain's most famous fashion exports. She hails from Croydon and ironically in October 2010, appeared on the cover of Bryan Ferry's Olympia."));


        logger.info(Joiner.on("\n").join(suggestions));


    }


    @Test
    public void testExtractSuggestion8() throws Exception {


        List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText(
                        "Roy Hodgson says Olympics is a 'wake-up call' for players and fans."));


        logger.info(Joiner.on("\n").join(suggestions));


    }


    @Test
    public void testScoringLogic() {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText(
                        "Kate is one of Britain's most famous fashion exports. She hails from Croydon and ironically in October 2010, appeared on the cover of Bryan Ferry's Olympia."));


        for (Suggestion suggestion : suggestions) {
            logger.info(suggestion.toString());
        }

        for (Suggestion suggestion : suggestions) {
            logger.info("Suggestion {} and score {}", suggestion.getTerm(), SuggestionScore.score(suggestion));
        }


    }


}
