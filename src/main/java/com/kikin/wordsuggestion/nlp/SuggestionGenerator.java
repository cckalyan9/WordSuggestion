package com.kikin.wordsuggestion.nlp;

import com.kikin.wordsuggestion.vo.Suggestion;
import edu.stanford.nlp.pipeline.Annotation;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/10/12
 * Time: 11:34 AM
 * ${END}$
 */
public interface SuggestionGenerator {
    List<Suggestion> extractSuggestion(final Annotation input);
}
