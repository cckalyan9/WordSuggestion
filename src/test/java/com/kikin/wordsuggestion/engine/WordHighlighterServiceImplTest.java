package com.kikin.wordsuggestion.engine;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.kikin.wordsuggestion.vo.WordSuggestionInput;
import com.kikin.wordsuggestion.vo.WordSuggestionInputBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 11:43 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/wordhighlighter.xml"})
public class WordHighlighterServiceImplTest {

    private static Logger logger = LoggerFactory.getLogger(WordHighlighterServiceImplTest.class);

    @Autowired
    WordHighlighterService wordHighlighterService;

    @Test
    public void testGetSuggestions() throws Exception {


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setSelectedText("is")
                .setContext("Stanford University is very great. I am in New York.Michael is a person")
                .setTextsNearSelectedItem(Lists.newArrayList("York")).createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);

        assertTrue(suggestions.size() > 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    //No Suggestion, since Michael is a proper Noun and in the present context not a stop word.
    @Test
    public void testGetSuggestions_NoSuggestion() throws Exception {


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setSelectedText("Michael")
                .setContext("Stanford University is very great. I am in New York.Michael is a person")
                .setTextsNearSelectedItem(Lists.newArrayList("York")).createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);

        assertTrue(suggestions.size() == 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    @Test
    public void testGetSuggestions_ForceSuggestion() throws Exception {


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setSelectedText("Michael")
                .setContext("Stanford University is very great. I am in New York.Michael is a person")
                .setTextsNearSelectedItem(Lists.newArrayList("York")).createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, true, 5);
        assertTrue(suggestions.size() > 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    @Test
    public void testGetSuggestions_LimitResult() throws Exception {


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setSelectedText("Michael")
                .setContext("Stanford University is very great. I am in New York.Michael is a person")
                .setTextsNearSelectedItem(Lists.newArrayList("York")).createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, true, 2);
        assertTrue(suggestions.size() == 2);
        logger.info(Joiner.on("\n").join(suggestions));
    }
}
