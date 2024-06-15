package vsdk.source;

/**
 * Range implementation.
 */
public class Range {
    /**
     * Create array range from `start` to `stop` with `step`.
     * Step inverses if start is bigger than end, so you can create reversed ranges.
     * End appended with step to make range end with actual set end.
     *
     * @param start Range start float.
     * @param end Range end float.
     * @param step Step float.
     */
    public static float[] newRange(float start, float end, float step) {
        if(start > end) step = -step;

        end += step;

        int rSize = (int) Math.ceil((end - start) / step);

        float[] range = new float[rSize];

        for(int index=0; index < rSize; index++) range[index] = start + (index * step);

        return range;
    }

    /**
     * Create array range from `start` to `stop` with `step`.
     * Step inverses if start is bigger than end, so you can create reversed ranges.
     * End appended with step to make range end with actual set end.
     * Works as same as <code>newRange</code> but reversed out range.
     *
     * @param start Range start float.
     * @param end Range end float.
     * @param step Step float.
     */
    public static float[] newRangeReverse(float start, float end, float step) {
        float[] range = newRange(start, end, step);

        for(int index=0; index < range.length / 2; index++) {
            float tFloat = range[index];

            range[index] = range[range.length - 1 - index];
            range[range.length - 1 - index] = tFloat;
        }

        return range;
    }
}