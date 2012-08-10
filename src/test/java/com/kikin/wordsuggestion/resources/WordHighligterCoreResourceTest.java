package com.kikin.wordsuggestion.resources;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 10:26 AM
 * Test case for the Rest Interface.
 */
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
    public void testGetSuggestions() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("core/suggestions").queryParam("selectedText", "hello")
                .queryParam("context", "This is where hello is situated").queryParam("fullText",
                        "This is a simple test").queryParam("appliedPressure", "1.2").queryParam(
                        "duration", "1.4").get(String.class);


        logger.info("Happy path {}", responseMsg);
        assertNotNull(responseMsg);
    }


    @Test(expected = UniformInterfaceException.class)
    public void testGetSuggestions_NullSelectedText() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("core/suggestions")
                .queryParam("context", "This is where hello is situated").queryParam("fullText",
                        "This is a simple test").queryParam("appliedPressure", "1.2").queryParam(
                        "duration", "1.4").get(String.class);

        // This block should be unreachable, since exception is expected

    }


    @Test(expected = UniformInterfaceException.class)
    public void testGetSuggestions_NullContext() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("core/suggestions").queryParam("selectedText", "hello")
                .queryParam("fullText",
                        "This is a simple test").queryParam("appliedPressure", "1.2").queryParam(
                        "duration", "1.4").get(String.class);


        // This block should be unreachable, since exception is expected
    }


    @Test
    public void testGetSuggestions_NullTouchParameters() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("core/suggestions").queryParam("selectedText", "hello")
                .queryParam("context", "This is where hello is situated").queryParam("fullText",
                        "This is a simple test").get(String.class);


        logger.info("Happy path {}", responseMsg);
        assertNotNull(responseMsg);
    }



}
