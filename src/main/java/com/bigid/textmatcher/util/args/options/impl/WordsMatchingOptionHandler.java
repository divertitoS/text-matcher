package com.bigid.textmatcher.util.args.options.impl;

import com.bigid.textmatcher.util.Constants;
import com.bigid.textmatcher.util.args.options.ApplicationOptionHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WordsMatchingOptionHandler implements ApplicationOptionHandler {
    private static final String OPTION_NAME = "words-matching";

    @Override
    public void handle(String rawOptionValue) {
        Constants.WORDS = Arrays.asList(rawOptionValue.split(","));
    }

    @Override
    public boolean isApplicable(String inputOptionName) {
        return OPTION_NAME.equals(inputOptionName);
    }
}
