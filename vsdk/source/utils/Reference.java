package vsdk.source.utils;

/**
 * Reference (getter/setter).
 */
public class Reference<T> {
    private T object;

    /**
     * Initialize reference.
     *
     * @param object_ Out object.
     */
    public Reference(T object_) {
        object = object_;
    }

    /**
     * Initialize reference.
     */
    public Reference() {
        object = null;
    }

    /**
     * Set object.
     *
     * @param object_ New object.
     */
    public void set(T object_) {
        object = object_;
    }

    /**
     * Get object.
     */
    public T get() {
        return object;
    }

    /**
     * Initialize Reference with object.
     *
     * @param object Object.
     */
    public static <T> Reference<T> ref(T object) {
        return new Reference<>(object);
    }
}
