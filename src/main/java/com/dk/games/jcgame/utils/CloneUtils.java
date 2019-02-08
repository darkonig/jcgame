package com.dk.games.jcgame.utils;

import com.dk.games.jcgame.model.Copy;

import java.util.*;
import java.util.stream.Collectors;

public class CloneUtils {

    public static<I,T extends Copy<T>> Map<I,T> deepCloneMap(Map<I,T> src) {
        return src.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().copy()));
    }

    public static<T extends Copy<T>> Set<T> deepCloneSet(Set<T> src) {
        return src.stream()
                .map(e -> e.copy())
                .collect(Collectors.toSet());
    }

    public static<T extends Copy<T>> SortedSet<T> deepCloneSortedSet(SortedSet<T> src) {
        return src.stream()
                .map(e -> e.copy())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public static<T extends Copy<T>> List<T> deepCloneList(List<T> src) {
        return src.stream()
                .map(e -> e.copy())
                .collect(Collectors.toList());
    }

}
