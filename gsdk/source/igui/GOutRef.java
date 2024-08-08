package gsdk.source.igui;

/**
 * Reference (getter/setter) for editable objects.
 */
public class GOutRef<T> {
    private T out;

    /**
     * Initialize reference.
     *
     * @param out_ Out object.
     */
    public GOutRef(T out_) {
        out = out_;
    }

    /**
     * Initialize reference.
     */
    public GOutRef() {
        out = null;
    }

    /**
     * Set object.
     *
     * @param object New object.
     */
    public void set(T object) {
        out = object;
    }

    /**
     * Get object.
     */
    public T get() {
        return out;
    }
}
