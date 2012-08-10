package com.kikin.wordsuggestion.resources;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kikin.wordsuggestion.engine.WordHighlighterService;
import com.kikin.wordsuggestion.vo.TouchParameters;
import com.kikin.wordsuggestion.vo.WordSuggestionInput;
import com.kikin.wordsuggestion.vo.WordSuggestionInputBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: Dec 27, 2010
 * Time: 2:35:36 PM
 * Perform operation over semantic vectors like search and compare terms.
 */
@Path("/core")
public class WordHighligterCoreResource {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    private final WordHighlighterService wordHighlighterService;

    public WordHighligterCoreResource() {


        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"wordhighlighter.xml"});

        wordHighlighterService = context.getBean("wordHighlighter", WordHighlighterService.class);

    }

    /**
     * @param selectedText     - The text highlighted in the touch based device
     * @param context          - The context of the highlighted text.
     * @param fullText         - The full text of the article.
     * @param textsInProximity - The list of texts that occurs close to the highlighted text ordered by proximity desc.Format: Json
     * @param appliedPressure  - The pressure applied as part of the touch interaction by the user.
     * @param duration         - The duration of the application of the pressure.
     * @return
     */
    @GET
    @Path("/suggestions")
    @Produces({MediaType.APPLICATION_JSON, "application/x-javascript"})
    public final String getWordSuggestions(@QueryParam("selectedText")
                                           final String selectedText, @QueryParam("context")
    final String context, @QueryParam("fullText")
    final String fullText, @QueryParam("textsInProximity") final String textsInProximity, @QueryParam("appliedPressure")
    final double appliedPressure, @QueryParam("duration")
    final double duration) {

        try {
            checkArgument(!Strings.isNullOrEmpty(selectedText), "Selected Text cannot be empty");
            checkArgument(!Strings.isNullOrEmpty(context), "Context cannot be empty");
            checkArgument(!Strings.isNullOrEmpty(fullText), "Full text cannot be empty");
        } catch (IllegalArgumentException e) {

            Response response = Response.status(
                    Response.Status.BAD_REQUEST).entity(e.getCause()).type("text/plain").build();
            throw new WebApplicationException(response);
        }


        logger.info("Entering getWordSuggestions for parameters {}, {}, {}",
                new String[]{selectedText, context, String.valueOf(fullText.length())});

        logger.info("Touch parameters {} , {}", appliedPressure, duration);

        Type collectionType = new TypeToken<Collection<String>>() {
        } // end new
                .getType();

        List<String> textsNearSelectedItem = new Gson().fromJson(textsInProximity, collectionType);


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setSelectedText(selectedText)
                .setContext(context).setFullText(fullText).setTouchParameters(new TouchParameters(appliedPressure,
                        duration)).setTextsNearSelectedItem(textsNearSelectedItem).createWordSuggestionInput();

        logger.info("Generated wordSuggestionInput {}", wordSuggestionInput.toString());

        List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);

        logger.info("Exiting getWordSuggestions");

        return new Gson().toJson(suggestions);


    }


}
