package be.ordina.cloudfoundry.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a {@link LinkedHashMap} with a fixed maximum size.
 *
 * @param <K> @see LinkedHashMap
 * @param <V> @see LinkedHashMap
 */
public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {

    private final int maxSize;

    public MaxSizeHashMap(final int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
