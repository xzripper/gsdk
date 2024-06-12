package vsdk.source;

/**
 * Utilities for asserting.
 */
public class Assert {
    /**
     * Throw assertion error if bool == false.
     *
     * @param bool Boolean.
     * @param message Assertion message.
     */
    public static void assert_f(boolean bool, String message) {
        if(!bool) {
            if(message != null) {
                throw new AssertionError(message);
            } else {
                throw new AssertionError();
            }
        }
    }

    /**
     * Throw assertion error if bool == false.
     *
     * @param bool Boolean.
     */
    public static void assert_f(boolean bool) {
        assert_f(bool, null);
    }

    /**
     * Throw assertion error if bool == true.
     *
     * @param bool Boolean.
     * @param message Assertion message.
     */
    public static void assert_t(boolean bool, String message) {
        if(bool) {
            if(message != null) {
                throw new AssertionError(message);
            } else {
                throw new AssertionError();
            }
        }
    }

    /**
     * Throw assertion error if bool == false.
     *
     * @param bool Boolean.
     */
    public static void assert_t(boolean bool) {
        assert_t(bool, null);
    }
}
