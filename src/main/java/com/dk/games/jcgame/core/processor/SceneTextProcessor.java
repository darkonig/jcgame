package com.dk.games.jcgame.core.processor;

import com.dk.games.jcgame.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SceneTextProcessor {

    private Pattern pattern = Pattern.compile("%.*?%");
    private Map<String, Supplier<String>> processors;

    public SceneTextProcessor() {
        processors = new HashMap<>();
    }

    public void addProcessor(String key, Supplier<String> processor) {
        processors.put(key, processor);
    }

    public String process(String src) {
        String text = src;
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String g = matcher.group();
            String replace = "";

            String cmdName = g.substring(1, g.length() - 1).trim();
            Supplier<String> textProcessor = processors.get(cmdName);
            if (textProcessor != null) {
                replace = textProcessor.get();
            }

            if (g.length() > replace.length()) {
                replace = StringUtils.padRight(replace, g.length());
            }

            text = text.replaceFirst(g, replace);
        }

        return text;
    }

}
