package com.bigid.textmatcher.util.args.options.impl;

import com.bigid.textmatcher.util.Constants;
import com.bigid.textmatcher.util.args.options.ApplicationOptionHandler;
import org.springframework.stereotype.Component;

@Component
public class SourcePathOptionHandler implements ApplicationOptionHandler {
    private static final String OPTION_NAME = "source-path";

    @Override
    public void handle(String rawOptionValue) {
        Constants.SOURCE_PATH = rawOptionValue;
    }

    @Override
    public boolean isApplicable(String inputOptionName) {
        return OPTION_NAME.equals(inputOptionName);
    }
}
