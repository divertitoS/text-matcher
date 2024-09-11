package com.bigid.textmatcher.util.args.options.impl;

import com.bigid.textmatcher.util.Constants;
import com.bigid.textmatcher.util.args.options.ApplicationOptionHandler;
import org.springframework.stereotype.Component;

@Component
public class BatchSizeOptionHandler implements ApplicationOptionHandler {
    private static final String OPTION_NAME = "batch-size";

    @Override
    public void handle(String rawOptionValue) {
        Constants.BATCH_SIZE = Integer.parseInt(rawOptionValue);
    }

    @Override
    public boolean isApplicable(String inputOptionName) {
        return OPTION_NAME.equals(inputOptionName);
    }
}
