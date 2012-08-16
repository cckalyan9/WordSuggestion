package com.kikin.wordsuggestion.resources;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kikin.wordsuggestion.engine.WordHighlighterService;
import com.kikin.wordsuggestion.vo.Rects;
import com.kikin.wordsuggestion.vo.TouchParameters;
import com.kikin.wordsuggestion.vo.WordSuggestionInput;
import com.kikin.wordsuggestion.vo.WordSuggestionInputBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
@Component
@Scope("prototype")
@Path("/core")
public class WordHighligterCoreResource {

    private static Logger logger = LoggerFactory.getLogger(WordHighligterCoreResource.class);


    @Autowired
    private WordHighlighterService wordHighlighterService;

    /*   public WordHighligterCoreResource() {


            ApplicationContext context = new ClassPathXmlApplicationContext(
                    new String[]{"wordhighlighter.xml"});

            wordHighlighterService = context.getBean("wordHighlighter", WordHighlighterService.class);

        }
    */


    @GET
    @Path("/test")
    @Produces({MediaType.APPLICATION_JSON, "application/x-javascript"})
    public final String test() {

        return "HELLO WORLD";
    }

    /**
     * Usage Example: http://localhost:8080/wordsuggestion/core/suggestions?context="Universal has decided to shelf its planned."
     * &rects=[{ 'word': 'Universal',  'top': 450,  'left': 120,  'right': 150,  'bottom': 470   },
     * {   'word': 'has',  'top': 450,  'left': 170,   'right': 180,  'bottom': 470  }]
     * &appliedPressure=1.2&duration=1.4&touchLocationX=165.0&touchLocationY=420.0
     *
     * @param context         - The context of the highlighted text.
     * @param fullText        - The full text of the article.
     * @param textBlocks      - The bounding rectangle of texts that occurs near touch location.Format: Json
     * @param appliedPressure - The pressure applied as part of the touch interaction by the user.
     * @param duration        - The duration of the application of the pressure.
     * @param touchLocationX  - X co-ordinates of the touch location.
     * @param touchLocationY  - Y co-ordinates of the touch location.
     * @return
     * @throws com.sun.jersey.api.client.UniformInterfaceException
     *          - 400 Bad Request, if Context, textBlocks, touchLocationX or touchLocationY is not set.
     */
    @GET
    @Path("/suggestions")
    @Produces({MediaType.APPLICATION_JSON, "application/x-javascript"})
    public final String getWordSuggestions(@QueryParam("context")
                                           final String context, @QueryParam("fullText")
    final String fullText, @QueryParam("rects") final String textBlocks,
                                           @QueryParam("touchLocationX") @DefaultValue("0.0")
                                           final double touchLocationX,
                                           @QueryParam("touchLocationY") @DefaultValue("0.0")
                                           final double touchLocationY,
                                           @QueryParam("appliedPressure")
                                           final double appliedPressure,
                                           @QueryParam("duration")
                                           final double duration
    )

    {

        try {
            checkArgument(!Strings.isNullOrEmpty(textBlocks), "Text blocks cannot be empty");
            checkArgument(!Strings.isNullOrEmpty(context), "Context cannot be empty");
            checkArgument(touchLocationY == 0.0, "Touch location is mandatory");
            checkArgument(touchLocationX == 0.0, "Touch location is mandatory");

        } catch (IllegalArgumentException e) {

            Response response = Response.status(
                    Response.Status.BAD_REQUEST).entity(e.getCause()).type("text/plain").build();
            throw new WebApplicationException(response);
        }


        Type collectionType = new TypeToken<Collection<Rects>>() {
        } // end new
                .getType();

        List<Rects> textsNearSelectedItem = new Gson().fromJson(textBlocks, collectionType);

        logger.info("Entering getWordSuggestions for parameters {}, {}",
                new String[]{textsNearSelectedItem.toString(), context});

        logger.info("Touch parameters {} , {}", appliedPressure, duration);


        WordSuggestionInput wordSuggestionInput = new WordSuggestionInputBuilder().setWords(textsNearSelectedItem)
                .setContext(context).setFullText(fullText).setTouchParameters(new TouchParameters(touchLocationX,
                        touchLocationY,
                        appliedPressure,
                        duration))
                        //.setWords(textsNearSelectedItem)
                .createWordSuggestionInput();

        logger.info("Generated wordSuggestionInput {}", wordSuggestionInput.toString());

        List<String> suggestions = wordHighlighterService.getSuggestions(wordSuggestionInput, false, 5);

        logger.info("Exiting getWordSuggestions");

        return new Gson().toJson(suggestions);


    }


}
