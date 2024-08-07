package gsdk.source.generic;

import java.util.Random;

import java.util.List;

/**
 * Utility class that makes work with random easier.
 */
public class VRandom {
    private static final Random random = new Random();

    /**
     * Set random class seed.
     *
     * @param seed Long seed.
     */
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    /**
     * Generates a random integer between min (inclusive) and max (inclusive).
     *
     * @param min The minimum value.
     * @param max The maximum value.
     */
    public static int randI(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * Generates a random float between min (inclusive) and max (inclusive).
     *
     * @param min The minimum value.
     * @param max The maximum value.
     */
    public static float randf(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    /**
     * Selects a random element from an array.
     *
     * @param array The array to select a random element from.
     */
    public static <T> T randomElement(T[] array) {
        return array[random.nextInt(array.length)];
    }

    /**
     * Selects a random element from a list.
     *
     * @param list The list to select a random element from.
     */
    public static <T> T randomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Selects a random element from an array based on weights.
     * Returns null in case array length is not equal to weights length.
     *
     * @param array The array to select a random element from.
     * @param weights The weights for each element in the array.
     */
    public static <T> T weightedRandomElement(T[] array, double ...weights) {
        if(array.length != weights.length) return null;

        double totalWeight = 0.0, cumulativeWeight = 0.0;

        for(double weight : weights) totalWeight += weight;

        double randomValue = randomDouble() * totalWeight;

        for(int i = 0; i < array.length; i++) {
            cumulativeWeight += weights[i];

            if(randomValue < cumulativeWeight) return array[i];
        }

        return array[array.length - 1];
    }

    /**
     * Shuffles an array in place.
     *
     * @param array The array to be shuffled.
     */
    public static <T> void shuffle(T[] array) {
        for(int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            T temp = array[index];

            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Generates a random permutation of integers from 0 (inclusive) to n (exclusive).
     *
     * @param uBound The upper bound (exclusive).
     */
    public static int[] permutation(int uBound) {
        int[] result = new int[uBound];

        for(int i = 0; i < uBound; i++) result[i] = i;

        for(int i = uBound - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            int temp = result[index];

            result[index] = result[i];
            result[i] = temp;
        }

        return result;
    }

    /**
     * Selects a specified number of unique elements from an array.
     * Returns null if count is more than array length.
     *
     * @param array The array to select elements from.
     * @param count The number of unique elements to select.
     */
    public static <T> T[] sample(T[] array, int count) {
        if(count > array.length) return null;

        T[] result = (T[]) new Object[count];
        T[] copy = array.clone();

        for(int i = 0; i < count; i++) {
            int index = i + random.nextInt(array.length - i);

            T temp = copy[i];

            copy[i] = copy[index];
            copy[index] = temp;

            result[i] = copy[i];
        }

        return result;
    }

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length The length of the random string.
     */
    public static String rndAlphaNumStr(int length) {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder result = new StringBuilder(length);

        for(int i = 0; i < length; i++) result.append(characters.charAt(random.nextInt(characters.length())));

        return result.toString();
    }

    /**
     * Generates a random integer between 0 (inclusive) and 1 (exclusive).
     */
    public static float randomInt() {
        return random.nextInt();
    }


    /**
     * Generates a random float between 0 (inclusive) and 1 (exclusive).
     */
    public static float randomFloat() {
        return random.nextFloat();
    }

    /**
     * Generates a random double between 0 (inclusive) and 1 (exclusive).
     */
    public static double randomDouble() {
        return random.nextDouble();
    }

    /**
     * Generates a random boolean value.
     */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    /**
     * Fills the specified array with random bytes.
     *
     * @param bytes The array to be filled with random bytes.
     */
    public static void randomBytes(byte[] bytes) {
        random.nextBytes(bytes);
    }

    /**
     * Generates a random Gaussian (normally) distributed double value with mean 0 and standard deviation 1.
     *
     * @return A random Gaussian distributed double value.
     */
    public static double randomGaussian() {
        return random.nextGaussian();
    }

    /**
     * Get random class.
     */
    public static Random getRandom() {
        return random;
    }
}
