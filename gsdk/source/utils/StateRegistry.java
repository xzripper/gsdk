package vsdk.source.utils;

import java.util.HashMap;

/**
 * State registry implementation.
 */
public class StateRegistry {
    private final HashMap<String, Boolean> registry = new HashMap<>();

    /**
     * Push key to registry.
     *
     * @param key Key.
     * @param value Value.
     */
    public void push(String key, boolean value) {
        registry.put(key, value);
    }

    /**
     * Push key with true value to registry.
     *
     * @param key Key.
     */
    public void pushT(String key) {
        push(key, true);
    }

    /**
     * Push key with false value to registry.
     *
     * @param key Key.
     */
    public void pushF(String key) {
        push(key, false);
    }

    /**
     * Get key. Returns false if there is no key registered like this.
     *
     * @param key Key.
     */
    public boolean get(String key) {
        return registry.getOrDefault(key, false);
    }
}
