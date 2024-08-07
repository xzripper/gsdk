package vsdk.source.ivui;

/**
 * Reference (getter/setter) for editable objects.
 */
public class VOutRef<T> {
    private T out;

    /**
     * Initialize reference.
     *
     * @param out_ Out object.
     */
    public VOutRef(T out_) {
        out = out_;
    }

    /**
     * Initialize reference.
     */
    public VOutRef() {
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
