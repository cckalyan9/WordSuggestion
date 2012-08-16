package com.kikin.wordsuggestion.resources;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 10:26 AM
 * Test case for the Rest Interface.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/wordhighlighter-test.xml"})
public class WordHighligterCoreResourceTest extends JerseyTest {


    private static Logger logger = LoggerFactory.getLogger(WordHighligterCoreResourceTest.class);


    public WordHighligterCoreResourceTest() throws Exception {
        super("com.kikin.wordsuggestion.resources");
    }


    /*@QueryParam("selectedText")
                                               final String selectedText, @QueryParam("context")
        final String context, @QueryParam("fullText")
        final String fullText, @QueryParam("appliedPressure")
        final double appliedPressure, @QueryParam("duration")
        final double duration
    */


    @Test
    public void testGetSuggestion_NewApi() {

        WebResource resource = resource();


    }


    /**
     * Happy Path . A simple term, with obvious stop words being selected.
     * <p/>
     * This test is to mainly see if the Rest interface handles request properly and check its validation logic.
     */
    @Test
    @Ignore
    public void testGetSuggestions() {
        WebResource webResource = resource();
        String jsonArr = " [{ 'word': 'Universal',  'top': 450,  'left': 120,  'right': 150,  'bottom': 470   }, "
                + "{   'word': 'has',  'top': 450,  'left': 170,   'right': 180,  'bottom': 470  }]";
        String responseMsg = webResource.path("core/suggestions").queryParam("context",
                "Universal has decided to shelf its planned.").queryParam("rects", jsonArr)
                .queryParam("appliedPressure", "1.2").queryParam(
                        "duration", "1.4").queryParam("touchLocationX", "165.0").queryParam("touchLocationY", "420.0")
                .get(String.class);


        logger.info("Happy path {}", responseMsg);
        assertNotNull(responseMsg);
    }


}
