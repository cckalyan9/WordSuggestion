package com.kikin.wordsuggestion.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/12/12
 * Time: 6:09 PM
 * ${END}$
 */
@Component
public class TestVariables {


    @Value("#{testProperties.suggestions}")
    private String suggestions;


    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }
}
