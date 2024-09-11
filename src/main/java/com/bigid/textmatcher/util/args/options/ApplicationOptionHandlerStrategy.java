package com.bigid.textmatcher.util.args.options;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationOptionHandlerStrategy {
    private final List<ApplicationOptionHandler> applicationOptionHandlers;

    public ApplicationOptionHandlerStrategy(List<ApplicationOptionHandler> applicationOptionHandlers) {
        this.applicationOptionHandlers = applicationOptionHandlers;
    }

    public ApplicationOptionHandler getHandler(String optionName) {
        return applicationOptionHandlers.stream()
                .filter(handler -> handler.isApplicable(optionName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such option handler. Option name: " + optionName));
    }
}
