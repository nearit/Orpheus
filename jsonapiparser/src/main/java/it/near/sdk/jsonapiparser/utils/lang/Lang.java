package it.near.sdk.jsonapiparser.utils.lang;

import java.util.Collections;

/**
 * @author Federico Boschini
 */
public class Lang {

    /**
     * Returns a safe array to iterate from.
     */
    public static <T> Iterable<T> safe(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }
}
