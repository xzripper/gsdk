package vsdk.source;

import com.raylib.Raylib;

import org.bytedeco.javacpp.BytePointer;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.nativeOrder;

import java.util.Random;

class WhiteN {
    private final Random random;
    protected WhiteN(int seed) {random = new Random(seed);}
    protected double noise() {return random.nextDouble();}}

class Perlin {
    private final int[] perm;
    private final int[] dPerm;
    protected Perlin(int seed) {
        perm = new int[256];dPerm = new int[512];
        Random random = new Random(seed);
        for(int i = 0; i < 256; i++) {perm[i] = i;}
        for(int i = 0; i < 256; i++) {int swapIndex = random.nextInt(256);int temp = perm[i];perm[i] = perm[swapIndex];perm[swapIndex] = temp;}
        for(int i = 0; i < 512; i++) {dPerm[i] = perm[i % 256];}}
    protected double noise(double x, double y, double z) {
        int X = (int) Math.floor(x) & 255;int Y = (int) Math.floor(y) & 255;int Z = (int) Math.floor(z) & 255;
        x -= Math.floor(x);y -= Math.floor(y);z -= Math.floor(z);
        double u = fade(x);double v = fade(y);double w = fade(z);
        int A = dPerm[X] + Y;int AA = dPerm[A] + Z;int AB = dPerm[A + 1] + Z;
        int B = dPerm[X + 1] + Y;int BA = dPerm[B] + Z;int BB = dPerm[B + 1] + Z;
        return lerp(w, lerp(v, lerp(u, grad(dPerm[AA], x, y, z), grad(dPerm[BA], x - 1, y, z)),
                        lerp(u, grad(dPerm[AB], x, y - 1, z), grad(dPerm[BB], x - 1, y - 1, z))),
                lerp(v, lerp(u, grad(dPerm[AA + 1], x, y, z - 1), grad(dPerm[BA + 1], x - 1, y, z - 1)),
                        lerp(u, grad(dPerm[AB + 1], x, y - 1, z - 1), grad(dPerm[BB + 1], x - 1, y - 1, z - 1))));}
    private double fade(double t) {return t * t * t * (t * (t * 6 - 15) + 10);}
    private double lerp(double t, double a, double b) {return a + t * (b - a);}
    private double grad(int hash, double x, double y, double z) {int h = hash & 15;double u = h < 8 ? x : y;double v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);}
    protected double fractalNoise(double x, double y, double z, int octaves, double persistence, double frequencyMult) {
        double total = 0;double frequency = 1;double amplitude = 1;double maxValue = 0;
        for(int i = 0; i < octaves; i++) {total += noise(x * frequency, y * frequency, z * frequency) * amplitude;maxValue += amplitude;amplitude *= persistence;frequency *= frequencyMult;}
        return total / maxValue;}
    protected double turbulenceNoise(double x, double y, double z, int octaves, double persistence, double frequencyMult) {
        double total = 0;double frequency = 1;double amplitude = 1;double maxValue = 0;
        for (int i = 0; i < octaves; i++) {total += Math.abs(noise(x * frequency, y * frequency, z * frequency) * amplitude);maxValue += amplitude;amplitude *= persistence;frequency *= frequencyMult;}
        return total / maxValue;}
    protected double ridgeNoise(double x, double y, double z, int octaves, double persistence, double frequencyMult) {
        double total = 0;double frequency = 1;double amplitude = 1;double maxValue = 0;
        for (int i = 0; i < octaves; i++) {double n = noise(x * frequency, y * frequency, z * frequency);total += (1 - Math.abs(n)) * amplitude;maxValue += amplitude;amplitude *= persistence;frequency *= frequencyMult;}
        return total / maxValue;}
    protected double billowNoise(double x, double y, double z, int octaves, double persistence, double frequencyMult) {
        double total = 0;double frequency = 1;double amplitude = 1;double maxValue = 0;
        for (int i = 0; i < octaves; i++) {double n = noise(x * frequency, y * frequency, z * frequency);total += (2 * Math.abs(n) - 1) * amplitude;maxValue += amplitude;amplitude *= persistence;frequency *= frequencyMult;}
        return total / maxValue;}}

class Simplex {
    private static final int[][] GRAD3 = {{1,1,0}, {-1,1,0}, {1,-1,0}, {-1,-1,0}, {1,0,1}, {-1,0,1}, {1,0,-1}, {-1,0,-1}, {0,1,1}, {0,-1,1}, {0,1,-1}, {0,-1,-1}};
    private static final int[] p = new int[512];
    private static final int[] perm = new int[512];
    protected Simplex(int seed) {
        Random random = new Random(seed);
        for(int i = 0; i < 256; i++) {p[i] = i;}
        for(int i = 255; i > 0; i--) {int n = random.nextInt(i + 1);int swap = p[i];p[i] = p[n];p[n] = swap;}
        for(int i = 0; i < 512; i++) {perm[i] = p[i & 255];}}
    protected double noise(double xin, double yin) {
        double n0, n1, n2;final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);double s = (xin + yin) * F2;int i = fastfloor(xin + s);
        int j = fastfloor(yin + s);final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;double t = (i + j) * G2;double X0 = i - t;double Y0 = j - t;
        double x0 = xin - X0;double y0 = yin - Y0;int i1, j1;if (x0 > y0) {i1 = 1; j1 = 0;} else {i1 = 0; j1 = 1;}double x1 = x0 - i1 + G2;double y1 = y0 - j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2;double y2 = y0 - 1.0 + 2.0 * G2;int ii = i & 255;int jj = j & 255;int gi0 = perm[ii + perm[jj]] % 12;
        int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;double t0 = 0.5 - x0 * x0 - y0 * y0;if (t0 < 0) n0 = 0.0;
        else {t0 *= t0;n0 = t0 * t0 * dot(GRAD3[gi0], x0, y0);}double t1 = 0.5 - x1 * x1 - y1 * y1;if (t1 < 0) n1 = 0.0;else {t1 *= t1;n1 = t1 * t1 * dot(GRAD3[gi1], x1, y1);}double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0) n2 = 0.0;else {t2 *= t2;n2 = t2 * t2 * dot(GRAD3[gi2], x2, y2);}return 70.0 * (n0 + n1 + n2);}
    private static int fastfloor(double x) {return x > 0 ? (int) x : (int) x - 1;}
    private static double dot(int[] g, double x, double y) {return g[0] * x + g[1] * y;}
    protected double fractalNoise(double x, double y, double z, int octaves, double persistence, double frequencyMult) {
        double total = 0;double frequency = 1;double amplitude = 1;double maxValue = 0;
        for (int i = 0; i < octaves; i++) {total += noise(x * frequency, y * frequency) * amplitude;maxValue += amplitude;amplitude *= persistence;frequency *= frequencyMult;}
        return total / maxValue;}
    protected double turbulenceNoise(double x, double y, double z, int octaves, double persistence, double frequencyMult) {
        double total = 0;double frequency = 1;double amplitude = 1;double maxValue = 0;
        for (int i = 0; i < octaves; i++) {total += Math.abs(noise(x * frequency, y * frequency)) * amplitude;maxValue += amplitude;amplitude *= persistence;frequency *= frequencyMult;}
        return total / maxValue;}
    protected double ridgeNoise(double x, double y, double z, int octaves, double persistence, double frequencyMult) {
        double total = 0;double frequency = 1;double amplitude = 1;double maxValue = 0;
        for (int i = 0; i < octaves; i++) {double n = noise(x * frequency, y * frequency);total += (1 - Math.abs(n)) * amplitude;maxValue += amplitude;amplitude *= persistence;frequency *= frequencyMult;}
        return total / maxValue;}
    protected double billowNoise(double x, double y, double z, int octaves, double persistence, double frequencyMult) {
        double total = 0;double frequency = 1;double amplitude = 1;double maxValue = 0;
        for (int i = 0; i < octaves; i++) {double n = noise(x * frequency, y * frequency);total += (2 * Math.abs(n) - 1) * amplitude;maxValue += amplitude;amplitude *= persistence;frequency *= frequencyMult;}
        return total / maxValue;}}

/**
 * VSDK Noise generator.
 */
public class NoiseGenerator {
    /**
     * Generate white noise.
     *
     * @param seed Seed.
     */
    public static double genWhiteNoise(int seed) {
        return new WhiteN(seed).noise();
    }

    /**
     * Generate perlin noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     */
    public static double genPerlinNoise(int seed, double x, double y, double z) {
        return new Perlin(seed).noise(x, y, z);
    }

    /**
     * Generate perlin fractal noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     */
    public static double genPerlinFractalNoise(int seed, double x, double y, double z, int octaves, float persistence, float frequencyMult) {
        return new Perlin(seed).fractalNoise(x, y, z, octaves, persistence, frequencyMult);
    }

    /**
     * Generate perlin turbulence noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     */
    public static double genPerlinTurbulenceNoise(int seed, double x, double y, double z, int octaves, float persistence, float frequencyMult) {
        return new Perlin(seed).fractalNoise(x, y, z, octaves, persistence, frequencyMult);
    }

    /**
     * Generate perlin ridge noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     */
    public static double genPerlinRidgeNoise(int seed, double x, double y, double z, int octaves, float persistence, float frequencyMult) {
        return new Perlin(seed).ridgeNoise(x, y, z, octaves, persistence, frequencyMult);
    }

    /**
     * Generate perlin billow noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     */
    public static double genPerlinBillowNoise(int seed, double x, double y, double z, int octaves, float persistence, float frequencyMult) {
        return new Perlin(seed).billowNoise(x, y, z, octaves, persistence, frequencyMult);
    }

    /**
     * Generate simplex noise.
     *
     * @param seed Seed.
     * @param xIn X.
     * @param yIn Y.
     */
    public static double genSimplexNoise(int seed, double xIn, double yIn) {
        return new Simplex(seed).noise(xIn, yIn);
    }

    /**
     * Generate simplex fractal noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     */
    public static double genSimplexFractalNoise(int seed, double x, double y, double z, int octaves, float persistence, float frequencyMult) {
        return new Simplex(seed).fractalNoise(x, y, z, octaves, persistence, frequencyMult);
    }

    /**
     * Generate simplex turbulence noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     */
    public static double genSimplexTurbulenceNoise(int seed, double x, double y, double z, int octaves, float persistence, float frequencyMult) {
        return new Simplex(seed).turbulenceNoise(x, y, z, octaves, persistence, frequencyMult);
    }

    /**
     * Generate simplex ridge noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     */
    public static double genSimplexRidgeNoise(int seed, double x, double y, double z, int octaves, float persistence, float frequencyMult) {
        return new Simplex(seed).ridgeNoise(x, y, z, octaves, persistence, frequencyMult);
    }

    /**
     * Generate simplex billow noise.
     *
     * @param seed Seed.
     * @param x X.
     * @param y Y.
     * @param z Z.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     */
    public static double genSimplexBillowNoise(int seed, double x, double y, double z, int octaves, float persistence, float frequencyMult) {
        return new Simplex(seed).billowNoise(x, y, z, octaves, persistence, frequencyMult);
    }

    /**
     * Generate white noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genWhiteNoiseImg(int seed, float scale, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        for (int i = 0; i < width * height; i++) {
            if((float) Raylib.GetRandomValue(0, 255) / 255 < scale) {
                buffer.put((byte) 0);
                buffer.put((byte) 0);
                buffer.put((byte) 0);
                buffer.put((byte) 255);
            } else {
                buffer.put((byte) 255);
                buffer.put((byte) 255);
                buffer.put((byte) 255);
                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate perlin noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genPerlinNoiseImg(int seed, int scale, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Perlin perlin = new Perlin(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = perlin.noise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale), 0);

                int pixelVal = (int) ((value + 1) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate perlin fractal noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     * @param pixAdd Append number to pixel (default is 1).
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genPerlinFractalNoiseImg(int seed, int scale, int octaves, float persistence, float frequencyMult, int pixAdd, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Perlin perlin = new Perlin(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = perlin.fractalNoise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale), 0, octaves, persistence, frequencyMult);

                int pixelVal = (int) ((value + pixAdd) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate perlin turbulence noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     * @param pixAdd Append number to pixel (default is 1).
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genPerlinTurbulenceNoiseImg(int seed, int scale, int octaves, float persistence, float frequencyMult, int pixAdd, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Perlin perlin = new Perlin(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = perlin.turbulenceNoise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale), 0, octaves, persistence, frequencyMult);

                int pixelVal = (int) ((value + pixAdd) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate perlin ridge noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     * @param pixAdd Append number to pixel (default is 1).
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genPerlinRidgeNoiseImg(int seed, int scale, int octaves, float persistence, float frequencyMult, int pixAdd, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Perlin perlin = new Perlin(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = perlin.ridgeNoise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale), 0, octaves, persistence, frequencyMult);

                int pixelVal = (int) ((value + pixAdd) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate perlin billow noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     * @param pixAdd Append number to pixel (default is 1).
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genPerlinBillowNoiseImg(int seed, int scale, int octaves, float persistence, float frequencyMult, int pixAdd, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Perlin perlin = new Perlin(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = perlin.billowNoise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale), 0, octaves, persistence, frequencyMult);

                int pixelVal = (int) ((value + pixAdd) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate simplex noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param pixAdd Append number to pixel (default is 1).
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genSimplexNoiseImg(int seed, int scale, int pixAdd, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Simplex simplex = new Simplex(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = simplex.noise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale));

                int pixelVal = (int) ((value + pixAdd) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate simplex turbulence noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     * @param pixAdd Append number to pixel (default is 1).
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genSimplexTurbulenceNoiseImg(int seed, int scale, int octaves, float persistence, float frequencyMult, int pixAdd, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Simplex simplex = new Simplex(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = simplex.turbulenceNoise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale), 0, octaves, persistence, frequencyMult);

                int pixelVal = (int) ((value + pixAdd) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate simplex ridge noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     * @param pixAdd Append number to pixel (default is 1).
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genSimplexRidgeNoiseImg(int seed, int scale, int octaves, float persistence, float frequencyMult, int pixAdd, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Simplex simplex = new Simplex(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = simplex.ridgeNoise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale), 0, octaves, persistence, frequencyMult);

                int pixelVal = (int) ((value + pixAdd) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Generate simplex billow noise image.
     *
     * @param seed Seed.
     * @param scale Scale.
     * @param octaves Octaves.
     * @param persistence Persistence.
     * @param frequencyMult Multiply frequency.
     * @param pixAdd Append number to pixel (default is 1).
     * @param width Width.
     * @param height Height.
     */
    public static Raylib.Image genSimplexBillowNoiseImg(int seed, int scale, int octaves, float persistence, float frequencyMult, int pixAdd, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(nativeOrder());

        Simplex simplex = new Simplex(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = simplex.billowNoise(scaleNoiseCoord((double) x / width, scale), scaleNoiseCoord((double) y / height, scale), 0, octaves, persistence, frequencyMult);

                int pixelVal = (int) ((value + pixAdd) * 127.5);

                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);
                buffer.put((byte) pixelVal);

                buffer.put((byte) 255);
            }
        }

        buffer.flip();

        return new Raylib.Image().data(new BytePointer(buffer)).width(width).height(height).mipmaps(1).format(Raylib.PIXELFORMAT_UNCOMPRESSED_R8G8B8A8);
    }

    /**
     * Scale noise coordinate.
     *
     * @param coord Coordinate.
     * @param scale Scale.
     */
    public static double scaleNoiseCoord(double coord, double scale) {
        return coord * scale;
    }

    /**
     * Load texture from noise image.
     *
     * @param nImg Noise image.
     */
    public static Raylib.Texture toTex(Raylib.Image nImg) {
        Raylib.Texture nTex = Raylib.LoadTextureFromImage(nImg);

        Raylib.UnloadImage(nImg);

        return nTex;
    }
}
