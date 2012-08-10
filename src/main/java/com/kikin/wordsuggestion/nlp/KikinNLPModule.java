package com.kikin.wordsuggestion.nlp;

import edu.stanford.nlp.pipeline.Annotation;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 3:37 PM
 * General interface that interacts with the NLP module to process input text.
 */
public interface KikinNLPModule {

    /**
     * This is the main method, that perform processing on the input text, and performs POS and NER among other things.
     *
     * @param context
     * @return
     */
    public Annotation processText(final String context);


    public boolean isStopWord(final String term);

    public List<String> filterStopWords(final String context) throws IOException;


}
