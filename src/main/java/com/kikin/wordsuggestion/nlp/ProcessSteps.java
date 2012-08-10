package com.kikin.wordsuggestion.nlp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 3:41 PM
 * Indicating all the possible process pipe lines for NLP processing.
 */
public enum ProcessSteps {

    TOKENIZE("tokenize"), SSPLIT("ssplit"), PARTS_OF_SPEECH("pos"), LEMMA("lemma"), NER("ner"), PARSE("parse"), DCOREF(
            "dcoref");

    // Constructor
    ProcessSteps(String p) {
        step = p;
    }

    private String step;

    public String getStepName() {
        return step;
    }

    @Override
    public String toString() {
        return step;
    }
}
