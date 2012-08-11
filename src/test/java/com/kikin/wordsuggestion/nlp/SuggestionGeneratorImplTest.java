package com.kikin.wordsuggestion.nlp;

import com.google.common.base.Joiner;
import com.kikin.wordsuggestion.utils.SuggestionScore;
import com.kikin.wordsuggestion.vo.Suggestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/10/12
 * Time: 11:34 AM
 * ${END}$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/wordhighlighter.xml"})
public class SuggestionGeneratorImplTest {

    private static Logger logger = LoggerFactory.getLogger(SuggestionGeneratorImplTest.class);

    @Autowired
    private SuggestionGenerator suggestionGenerator;

    //TODO: Replace concrete implementation with Stub. The Context returned by Standford NLP is very hard to Mock.
    @Qualifier("nlpModuleWithAllProcesses")
    @Autowired
    private KikinNLPModule kikinNLPModule;

    @Test
    public void testExtractSuggestion() throws Exception {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText("Stanford University is very good. I am in New York.Michael is a person"));

        logger.info(Joiner.on("\n").join(suggestions));


    }


    @Test
    public void testExtractSuggestion1() throws Exception {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText("I love the movie, 'Pirates of the Carribean'."));

        logger.info(Joiner.on("\n").join(suggestions));


    }


    @Test
    public void testExtractSuggestion2() throws Exception {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText("Linear regression is a very good hypothesis tester."));

        logger.info(Joiner.on("\n").join(suggestions));


    }


    @Test
    public void testExtractSuggestion3() throws Exception {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText("The."));

        logger.info(Joiner.on("\n").join(suggestions));


    }

    @Test
    public void testExtractSuggestion4() throws Exception {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText(
                        "Dog saves all her puppies from a house fire, and put them to safety in one of firetrucks."));

        logger.info(Joiner.on("\n").join(suggestions));


    }


    @Test
    public void testExtractSuggestion5() throws Exception {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText(
                        "Stanford University is very good. I am in New York.Michael is a person."));

        logger.info(Joiner.on("\n").join(suggestions));


        Collections.sort(suggestions);

        logger.info(Joiner.on("\n").join(suggestions));


    }


    @Test
    public void testExtractSuggestion6() throws Exception {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText(
                        "On the face of it, Senator Harry Reid’s explosive but flimsily sourced claim that Mitt Romney paid no income tax seems preposterous."));

        logger.info(Joiner.on("\n").join(suggestions));


        Collections.sort(suggestions);

        logger.info(Joiner.on("\n").join(suggestions));


    }


    @Test
    public void testScoringLogic() {

        final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(
                kikinNLPModule.processText(
                        "On the face of it, Senator Harry Reid’s explosive but flimsily sourced claim that Mitt Romney paid no income tax seems preposterous."));


        for (Suggestion suggestion : suggestions) {
            logger.info(suggestion.toString());
        }

        for (Suggestion suggestion : suggestions) {
            logger.info("Suggestion {} and score {}", suggestion.getTerm(), SuggestionScore.score(suggestion));
        }


    }


}
