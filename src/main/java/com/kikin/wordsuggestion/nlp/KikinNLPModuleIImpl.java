package com.kikin.wordsuggestion.nlp;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.logging.StanfordRedwoodConfiguration;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 3:46 PM
 * Implementation of the NLP Module.
 */

public class KikinNLPModuleIImpl implements KikinNLPModule {


    private static Logger logger = LoggerFactory.getLogger(KikinNLPModuleIImpl.class);


    private String DELIMITER = "";
    private final StanfordCoreNLP stanfordCoreNLP;
    private final Set<?> englishStopWordsSet;
    private final Set<String> auxStopWordsList;


    /**
     * @param nlpProcesses     - Indicates the list of processed that the NLP module shoudl perform
     * @param stopWordResource - Any additional stop words list.
     */
    public KikinNLPModuleIImpl(List<ProcessSteps> nlpProcesses, Resource stopWordResource) {

        Properties nlpProperties = new Properties();

        StringBuilder processSteps = new StringBuilder();
        for (ProcessSteps nlpProcess : nlpProcesses) {
            processSteps.append(DELIMITER).append(nlpProcess.getStepName());
            if (DELIMITER.isEmpty())
                DELIMITER = ",";
        }

        nlpProperties.setProperty("annotators", processSteps.toString());

        StanfordRedwoodConfiguration.minimalSetup();
        stanfordCoreNLP = new StanfordCoreNLP(nlpProperties);

        englishStopWordsSet = StandardAnalyzer.STOP_WORDS_SET;
        auxStopWordsList = Sets.newHashSet();

        if (stopWordResource != null) {

            try {
                List<String> auxStopWords = Files.readLines(stopWordResource.getFile(), Charset.defaultCharset());
                for (String string : auxStopWords) {

                    auxStopWordsList.add(string.toLowerCase());

                }
            } catch (IOException e) {
                logger.error(" Exception occurred while loading stopwords list  " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public Annotation processText(String context) {

        return stanfordCoreNLP.process(context);
    }


    /**
     * This particular implementation uses Standard English stop words dictionary. More often than Not stopwords are
     * domain specific.  The easiest way is to use a domain specific method.
     * TODO: Investigate a more domain specific stop words logic.
     *
     * @param term
     * @return
     */
    @Override
    public boolean isStopWord(@Nonnull final String term) {

        if (Strings.isNullOrEmpty(term) || term.trim().isEmpty())
            return true;
        return englishStopWordsSet.contains(term.toLowerCase()) || auxStopWordsList.contains(term.toLowerCase());
    }

    @Override
    public List<String> filterStopWords(String context) throws IOException {


        List<String> tokens = Lists.newArrayList();
        TokenStream tokenStream = new StandardAnalyzer(Version.LUCENE_34).tokenStream("contents", new StringReader(
                context));
        TermAttribute term = (TermAttribute) tokenStream.addAttribute(TermAttribute.class);
        while (tokenStream.incrementToken()) {
            tokens.add(term.term());
        }

        return tokens;


    }


}


