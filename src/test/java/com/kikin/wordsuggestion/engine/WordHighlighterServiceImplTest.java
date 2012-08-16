package com.kikin.wordsuggestion.engine;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kikin.wordsuggestion.vo.Rects;
import com.kikin.wordsuggestion.vo.TouchParameters;
import com.kikin.wordsuggestion.vo.WordSuggestionInput;
import com.kikin.wordsuggestion.vo.WordSuggestionInputBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 11:43 AM
 * TODO: Need more test cases for checking functionality.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/wordhighlighter-test.xml"})
public class WordHighlighterServiceImplTest {

    private static Logger logger = LoggerFactory.getLogger(WordHighlighterServiceImplTest.class);

    @Autowired
    WordHighlighterService wordHighlighterService;

    /**
     * Simple scenario, where the user selected on obvious stop word and there is an obvious candidate near by.
     *
     * @throws Exception
     */
    @Test
    public void testGetSuggestions_HappyPath() throws Exception {

        Type collectionType = new TypeToken<Collection<Rects>>() {
        } // end new
                .getType();

        String jsonArr = " [{ 'word': 'Universal',  'top': 450,  'left': 120,  'right': 150,  'bottom': 470   }, {   'word': 'has',  'top': 450,  'left': 170,   'right': 180,  'bottom': 470  }]";
        List<Rects> textsNearSelectedItem = new Gson().fromJson(jsonArr, collectionType);
// Touch parameters are closer to the term "has" than to "Universal"
        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setWords(textsNearSelectedItem)
                .setContext("Universal has decided to shelf its planned")
//                .setTextsNearSelectedItem(Lists.newArrayList("York"))
                .setTouchParameters(new TouchParameters(165.0, 420.0, 0, 0))
                .createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);
        assertTrue(suggestions.size() > 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    /**
     * Slightly more complicated scenario, with words wrapped and occurrence of a term, that is not part of the context.
     *
     * @throws Exception
     */
    @Test
    public void testGetSuggestions_MuddledWordProximity() throws Exception {

        Type collectionType = new TypeToken<Collection<Rects>>() {
        } // end new
                .getType();

        String jsonArr = " [{ 'word': 'Watts',  'top': 200,  'left': 120,  'right': 140,  'bottom': 220   },{ 'word': 'in',  'top': 200,  'left': 150,  'right': 160,  'bottom': 220   }, "
                + "{   'word': 'Naomi',  'top': 200,  'left': 90,   'right': 105,  'bottom': 220  }, "
                + "{   'word': 'Hollywood',  'top': 240,  'left': 110,   'right': 140,  'bottom': 260  }]";
        List<Rects> textsNearSelectedItem = new Gson().fromJson(jsonArr, collectionType);
        // Touch parameters are closer to the term "has" than to "Universal"
        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setWords(textsNearSelectedItem)
                .setContext("Robert Pattinson joining Naomi Watts  in 'Queen of the Desert.")
                        // Touch set closest to In.
                .setTouchParameters(new TouchParameters(155.0, 203.0, 0, 0))
                .createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);
        assertTrue(suggestions.size() > 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }

    /**
     * User clicks between two words carrying high entropy score.
     *
     * @throws Exception
     */
    @Test
    public void testGetSuggestions_Ambiguity() throws Exception {

        Type collectionType = new TypeToken<Collection<Rects>>() {
        } // end new
                .getType();

        String jsonArr = " [{ 'word': 'Chris',  'top': 230,  'left': 140,  'right': 170,  'bottom': 250   },"
                + "{ 'word': 'Huntsman',  'top': 200,  'left': 230,  'right': 250,  'bottom': 220   }, "
                + "{   'word': 'Hemsworth',  'top': 230,  'left': 180,   'right': 210,  'bottom': 250  }, "
                + "{   'word': 'Kristen',  'top': 230,  'left': 220,   'right': 240,  'bottom': 250  }, "
                + "{   'word': 'Stewart',  'top': 230,  'left': 250,   'right': 270,  'bottom': 250  }, "
                + "{'word': 'Sequel',  'top': 200,  'left': 270,   'right': 290,  'bottom': 220  }"
                + "]";


        List<Rects> textsNearSelectedItem = new Gson().fromJson(jsonArr, collectionType);
        // Touch parameters are Slightly closer to Kristen than to Huntsman
        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setWords(textsNearSelectedItem)
                .setContext(
                        "Universal has decided to shelf its planned Snow White and the Huntsman sequel and is instead \"\n"
                                + "                                + \"focusing on a solo Huntsman  movie starring Chris Hemsworth.Kristen Stewart will not be invited to return if the follow up \"\n"
                                + "                                + \"goes forward")
                        // Touch set closest to In.
                .setTouchParameters(new TouchParameters(235.0, 224.0, 0, 0))
                .createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);
        assertTrue(suggestions.size() > 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    /**
     * Same as testGetSuggestions_Ambiguity, but this time the touch point is slightly closer to Kristen than Huntsman
     *
     * @throws Exception
     */
    @Test
    public void testGetSuggestions_Ambiguity2() throws Exception {

        Type collectionType = new TypeToken<Collection<Rects>>() {
        } // end new
                .getType();

        String jsonArr = " [{ 'word': 'Chris',  'top': 230,  'left': 140,  'right': 170,  'bottom': 250   },"
                + "{ 'word': 'Huntsman',  'top': 200,  'left': 230,  'right': 250,  'bottom': 220   }, "
                + "{   'word': 'Hemsworth',  'top': 230,  'left': 180,   'right': 210,  'bottom': 250  }, "
                + "{   'word': 'Kristen',  'top': 230,  'left': 220,   'right': 240,  'bottom': 250  }, "
                + "{   'word': 'Stewart',  'top': 230,  'left': 250,   'right': 270,  'bottom': 250  }, "
                + "{'word': 'Sequel',  'top': 200,  'left': 270,   'right': 290,  'bottom': 220  }"
                + "]";


        List<Rects> textsNearSelectedItem = new Gson().fromJson(jsonArr, collectionType);
        // Touch parameters are Slightly closer to Kristen than to Huntsman
        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setWords(textsNearSelectedItem)
                .setContext(
                        "Universal has decided to shelf its planned Snow White and the Huntsman sequel and is instead \"\n"
                                + "                                + \"focusing on a solo Huntsman  movie starring Chris Hemsworth.Kristen Stewart will not be invited to return if the follow up \"\n"
                                + "                                + \"goes forward")
                        // Touch set closest to In.
                .setTouchParameters(new TouchParameters(235.0, 226.0, 0, 0))
                .createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);
        assertTrue(suggestions.size() > 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    /**
     * In this case, the user have clearly selected Kristen and hence the system will not provide any suggestion, unless the
     * force suggestion parameter is true.
     *
     * @throws Exception
     */
    @Test
    public void testGetSuggestions_ClearIntent_NoSuggestion() throws Exception {

        Type collectionType = new TypeToken<Collection<Rects>>() {
        } // end new
                .getType();

        String jsonArr = " [{ 'word': 'Chris',  'top': 230,  'left': 140,  'right': 170,  'bottom': 250   },"
                + "{ 'word': 'Huntsman',  'top': 200,  'left': 230,  'right': 250,  'bottom': 220   }, "
                + "{   'word': 'Hemsworth',  'top': 230,  'left': 180,   'right': 210,  'bottom': 250  }, "
                + "{   'word': 'Kristen',  'top': 230,  'left': 220,   'right': 240,  'bottom': 250  }, "
                + "{   'word': 'Stewart',  'top': 230,  'left': 250,   'right': 270,  'bottom': 250  }, "
                + "{'word': 'Sequel',  'top': 200,  'left': 270,   'right': 290,  'bottom': 220  }"
                + "]";


        List<Rects> textsNearSelectedItem = new Gson().fromJson(jsonArr, collectionType);
        // Touch parameters are Slightly closer to Kristen than to Huntsman
        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setWords(textsNearSelectedItem)
                .setContext(
                        "Universal has decided to shelf its planned Snow White and the Huntsman sequel and is instead \"\n"
                                + "                                + \"focusing on a solo Huntsman  movie starring Chris Hemsworth.Kristen Stewart will not be invited to return if the follow up \"\n"
                                + "                                + \"goes forward")
                        // Touch set closest to In.
                .setTouchParameters(new TouchParameters(230.0, 240.0, 0, 0))
                .createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);
        assertTrue(suggestions.size() == 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    /*//No Suggestion, since Michael is a proper Noun and in the present context not a stop word.
    @Test
    public void testGetSuggestions_NoSuggestion() throws Exception {


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setSelectedText("Michael")
                .setContext("Stanford University is very great. I am in New York.Michael is a person")
//                .setTextsNearSelectedItem(Lists.newArrayList("York"))
                .createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);

        assertTrue(suggestions.size() == 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    @Test
    public void testGetSuggestions_ForceSuggestion() throws Exception {


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setSelectedText("Michael")
                .setContext("Stanford University is very great. I am in New York.Michael is a person")
//                .setTextsNearSelectedItem(Lists.newArrayList("York"))
                .createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, true, 5);
        assertTrue(suggestions.size() > 0);
        logger.info(Joiner.on("\n").join(suggestions));
    }


    @Test
    public void testGetSuggestions_LimitResult() throws Exception {


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setSelectedText("Michael")
                .setContext("Stanford University is very great. I am in New York.Michael is a person")
//                .setTextsNearSelectedItem(Lists.newArrayList("York"))
                .createWordSuggestionInput();
        final List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, true, 2);
        assertTrue(suggestions.size() == 2);
        logger.info(Joiner.on("\n").join(suggestions));
    }*/
}
