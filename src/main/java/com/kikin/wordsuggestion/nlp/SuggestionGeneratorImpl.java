package com.kikin.wordsuggestion.nlp;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kikin.wordsuggestion.utils.StringConstants;
import com.kikin.wordsuggestion.vo.Suggestion;
import com.kikin.wordsuggestion.vo.filters.NounPosFilter;
import com.kikin.wordsuggestion.vo.filters.SuggestionTextFilter;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nullable;
import java.util.*;


public class SuggestionGeneratorImpl implements SuggestionGenerator {

    private static Logger logger = LoggerFactory.getLogger(SuggestionGeneratorImpl.class);
    private static final int MAXIMUM_NOUN_PHRASE_SIZE = 6;


    // Although not static, the value injected variables should be treated as such.
    @Value("#{posProperties.NOUN}")
    private String NOUN;
    @Value("#{posProperties.NOUN_COMPOUND_MODIFIER}")
    private String NOUN_COMPOUND_MODIFIER;
    @Value("#{posProperties.NOUN_PHRASE}")
    private String NOUN_PHRASE;
    private static Predicate<? super Suggestion> termFilter;
    private static final Function<? super Suggestion, String> getSuggestionTexts = new Function<Suggestion, String>() {
        @Override
        public String apply(@Nullable Suggestion suggestion) {
            return suggestion.getTerm().trim();
        }
    };
    private static final Predicate<? super String> nounPosFilter = new NounPosFilter();


    @Override
    public List<Suggestion> extractSuggestion(Annotation annotation) {

        List<Suggestion> suggestionList = Lists.newArrayList();
        Map<Integer, Suggestion> suggestions = Maps.newHashMap();
        extractNouns(annotation, suggestions);
        final List<Suggestion> nounPhrases = Lists.newArrayList();
        extractNounPhrases(annotation, suggestions, nounPhrases);

        suggestionList.addAll(Sets.newHashSet(suggestions.values()));

        final SuggestionTextFilter suggestionTextFilter = new SuggestionTextFilter(
                Collections2.transform(suggestionList, getSuggestionTexts));


        final Collection<Suggestion> nounPhrasesNotIncluded = Collections2.filter(nounPhrases,
                suggestionTextFilter);


        suggestionList.addAll(nounPhrasesNotIncluded);

        Collections.sort(suggestionList);

        return suggestionList;
    }


    /**
     * Private methods *
     */

    private void extractNouns(Annotation process, Map<Integer, Suggestion> suggestions) {
        final List<CoreLabel> coreLabels = process.get(CoreAnnotations.TokensAnnotation.class);
        for (CoreLabel coreLabel : coreLabels) {

            final String partsOfSpeech = coreLabel.get(
                    CoreAnnotations.PartOfSpeechAnnotation.class);
            logger.trace("Core Label {}, {}, {}",
                    new String[]{coreLabel.value(), partsOfSpeech, coreLabel.get(
                            CoreAnnotations.NamedEntityTagAnnotation.class)});

            if (partsOfSpeech != null && partsOfSpeech.startsWith(NOUN))
                suggestions.put(coreLabel.get(
                        CoreAnnotations.TokenBeginAnnotation.class), Suggestion.createSuggestion(coreLabel.value(),
                        partsOfSpeech, coreLabel.get(
                        CoreAnnotations.NamedEntityTagAnnotation.class), coreLabel.get(
                        CoreAnnotations.TokenBeginAnnotation.class)));


        }
    }

    private void extractNounPhrases(Annotation process, Map<Integer, Suggestion> suggestions,
                                    List<Suggestion> nounPhrases) {
        final List<CoreMap> coreMaps = process.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap coreMap : coreMaps) {

            logger.trace("CoreMap {}", coreMap.toString());

            final SemanticGraph semanticGraph = coreMap.get(
                    SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);

            final Set<SemanticGraphEdge> edgeSet = semanticGraph.getEdgeSet();
            for (SemanticGraphEdge semanticGraphEdge : edgeSet) {

                final IndexedWord source = semanticGraphEdge.getSource();
                final IndexedWord target = semanticGraphEdge.getTarget();
                logger.trace("Found relationship between {} and target {}", source,
                        target);
                final GrammaticalRelation relation = semanticGraphEdge.getRelation();
                logger.trace("The relationship is {}", relation);

                if (relation.equals(GrammaticalRelation.valueOf(NOUN_COMPOUND_MODIFIER))) {

                    final Integer sourceOffset = source.get(CoreAnnotations.TokenBeginAnnotation.class);
                    final Integer targetOffSet = target.get(CoreAnnotations.TokenBeginAnnotation.class);
                    logger.trace("The sourceoffset {} and targetoffset {}", sourceOffset, targetOffSet);
                    final Suggestion sourceSuggestion = suggestions.get(sourceOffset);
                    final Suggestion targetSuggestion = suggestions.get(targetOffSet);
                    if (sourceSuggestion != null && targetSuggestion != null) {
                        final Suggestion merge = sourceSuggestion.merge(targetSuggestion);
                        // Add this new entry in place of the individual entries.
                        // Essentially we are adding duplicate copies, for the equals method to handle this retropectively. This reduces the
                        //programming complexity.
                        suggestions.put(Math.min(sourceOffset, targetOffSet), merge);
                        suggestions.put(Math.max(sourceOffset, targetOffSet), merge);
                        /*   //remove the other entry

                        final int removedIndex = Math.max(sourceOffset, targetOffSet);
                        suggestions.remove(removedIndex);*/
                    }


                }


            }
            final Tree trees = coreMap.get(TreeCoreAnnotations.TreeAnnotation.class);
            logger.info(trees.toString());

            nounPhrases.addAll(extractPhrasesFromParsedTree(trees, Lists.<Suggestion>newArrayList()));
            //logger.trace(semanticGraph.toCompactString());


        }
    }

    private List<Suggestion> extractPhrasesFromParsedTree(Tree trees, List<Suggestion> nounPhrases) {

        final String label = trees.label().value();

        if (label != null && label.equals(NOUN_PHRASE)) {
            logger.trace(label);
            final List<String> posTagging = Lists.newArrayList();
            final Suggestion childrensAsPhrases = getChildrensAsPhrases(trees.children(), new StringBuilder(),
                    posTagging);

            final Collection<String> nounConstructs = Collections2.filter(posTagging, nounPosFilter);
            if (nounConstructs.size() > 1 && (childrensAsPhrases.getTerm().split(
                    " ").length < MAXIMUM_NOUN_PHRASE_SIZE)) {

                nounPhrases.add(childrensAsPhrases);
            }
        }
        Tree[] kids = trees.children();
        if (kids != null) {
            for (Tree kid : kids) {
                extractPhrasesFromParsedTree(kid, nounPhrases);
            }
        }

        return nounPhrases;

    }

    private Suggestion getChildrensAsPhrases(Tree[] children, StringBuilder child, List<String> posTagging) {

        for (int i = 0; i < children.length; i++) {
            final Tree child1 = children[i];
            if (!child1.toString().startsWith("("))
                child.append(child1.label().value()).append(StringConstants.SPACE);
            else
                posTagging.add(child1.label().value());


            if (child1.children() != null) {
                getChildrensAsPhrases(child1.children(), child, posTagging);
            }
        }
        return Suggestion.createSuggestionWithJustTerm(child.toString());

    }

    /** End of private methods **/


}