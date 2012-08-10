package com.kikin.wordsuggestion.engine;

import com.kikin.wordsuggestion.vo.WordSuggestionInput;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 11:11 AM
 * WordHighLighter, main interface.
 */
public interface WordHighlighterService {


    public List<String> getSuggestions(final WordSuggestionInput wordSuggestionInput, boolean forceSuggestion,
                                       int maxResult);

}
