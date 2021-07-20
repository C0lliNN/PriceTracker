package com.raphaelcollin.pricetracker;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.join;

public class ArgumentsParser {
    private final Map<String, String> paramsAndValues = new HashMap<>();

    public ArgumentsParser(final String[] args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException(format("The following CLI arguments is not valid: %s", join(" ", args)));
        }

        parse(args);
    }

    private void parse(final String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            String parameter = args[i].replace("-", "");
            String value = args[i + 1];

            paramsAndValues.put(parameter, value);
        }
    }

    public String getParameter(String parameter) {
        return paramsAndValues.get(parameter);
    }
}
