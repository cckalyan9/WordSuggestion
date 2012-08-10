package com.kikin.wordsuggestion.engine;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.kikin.wordsuggestion.nlp.KikinNLPModule;
import com.kikin.wordsuggestion.nlp.SuggestionGeneratorImpl;
import com.kikin.wordsuggestion.vo.Suggestion;
import com.kikin.wordsuggestion.vo.WordSuggestionInput;
import com.kikin.wordsuggestion.vo.filters.ProximityTextFilter;
import edu.stanford.nlp.pipeline.Annotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 11:15 AM
 * Implementation of the WordHighlighter.
 */
@Component("wordHighlighter")
public class WordHighlighterServiceImpl implements WordHighlighterService {


    @Qualifier("nlpModuleWithAllProcesses")
    @Autowired
    private KikinNLPModule kikinNLPModule;

    @Autowired
    private SuggestionGeneratorImpl suggestionGenerator;

    /**
     * Method will return empty collection, if it deems that there is no need for suggestion.
     *
     * @param wordSuggestionInput
     * @param forceSuggestion
     * @param maxResult           @return
     */
    @Override
    public List<String> getSuggestions(WordSuggestionInput wordSuggestionInput, boolean forceSuggestion,
                                       int maxResult) {
        checkNotNull(wordSuggestionInput);
        //Check the entropy of the highlighted word, provide suggestion if the word is in a set of commmon stop words.
        final boolean stopWord = kikinNLPModule.isStopWord(wordSuggestionInput.getSelectedText());
        final ArrayList<String> returnValue = Lists.newArrayList();
        if (stopWord || forceSuggestion) {
            // Get suggestion based on context.
            final Annotation annotation = kikinNLPModule.processText(wordSuggestionInput.getContext());
            final List<Suggestion> suggestions = suggestionGenerator.extractSuggestion(annotation);

            //Iterate through the texts close to the selected text.  The logic is order based on proximity, then
            // order based on Suggestion score, than is based on NLP constructs.

            //TODO: Investigate if this logic will work, or do we need to perform some kind of weighted average.
            for (String near : wordSuggestionInput.getTextsNearSelectedItem()) {
                final List<Suggestion> filteredSuggestion = Lists.newArrayList(Collections2.filter(suggestions,
                        new ProximityTextFilter(near)));
                Collections.sort(filteredSuggestion);

                for (Suggestion suggestion : filteredSuggestion) {
                    if (!returnValue.contains(suggestion)) {
                        returnValue.add(suggestion.getTerm());
                    }
                }
            }

            //Add the rest of the suggestions.

            Collections.sort(suggestions);

            for (Suggestion suggestion : suggestions) {
                if (!returnValue.contains(suggestion.getTerm())) {
                    returnValue.add(suggestion.getTerm());
                }
            }


        } else {
            return Lists.newArrayList();
        }


        return Lists.partition(returnValue, maxResult).get(0);
    }
}
