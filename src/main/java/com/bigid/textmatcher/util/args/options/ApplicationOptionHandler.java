package com.bigid.textmatcher.util.args.options;

public interface ApplicationOptionHandler {
    void handle(String rawOptionValue);

    boolean isApplicable(String inputOptionName);
}
