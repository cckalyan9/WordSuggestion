package com.kikin.wordsuggestion.nlp;

import edu.stanford.nlp.pipeline.Annotation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 4:32 PM
 * Test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/wordhighlighter.xml"})
public class KikinNLPModuleIImplTest {


    private static Logger logger = LoggerFactory.getLogger(KikinNLPModuleIImplTest.class);

    @Qualifier("nlpModuleWithAllProcesses")
    @Autowired
    private KikinNLPModule kikinNLPModule;

    @Test
    public void testProcessText() throws Exception {

        final Annotation annotation = kikinNLPModule.processText(
                "Stanford University is very good. It's not in New York");
        assertNotNull(annotation);

    }


    @Test
    public void testProcessText_Parse() throws Exception {

        final Annotation annotation = kikinNLPModule.processText(
                "Stanford University is very good. It's not in New York"
        );
        assertNotNull(annotation);

    }


    @Test
    public void testStopWord() {

        assertTrue(kikinNLPModule.isStopWord("the"));
        assertTrue(kikinNLPModule.isStopWord("a"));
        assertTrue(kikinNLPModule.isStopWord("If"));
        assertTrue(kikinNLPModule.isStopWord("to"));
        assertTrue(kikinNLPModule.isStopWord("with"));
        assertFalse(kikinNLPModule.isStopWord("Anemic"));
        assertFalse(kikinNLPModule.isStopWord("new"));
        assertTrue(kikinNLPModule.isStopWord(""));
        assertTrue(kikinNLPModule.isStopWord("  "));
        assertTrue(kikinNLPModule.isStopWord(null));


    }


    @Test
    public void testRemoveStopWords() throws IOException {

        List<String> tokens = kikinNLPModule.filterStopWords("I am the man, who nobody wants to mess with");

        logger.info("Returned token after filetering {}", tokens.toString());

        assertFalse(tokens.contains("the") || tokens.contains("to") || tokens.contains("with"));

        tokens = kikinNLPModule.filterStopWords("I am THe man, who nobody wants to mess with");


        logger.info("Returned token after filetering {}", tokens.toString());

        assertFalse(tokens.contains("THe") || tokens.contains("to") || tokens.contains("with"));
    }
}
