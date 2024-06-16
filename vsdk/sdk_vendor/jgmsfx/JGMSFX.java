// JGMSFX: Subproject of GMSFX3.

package vsdk.sdk_vendor.jgmsfx;

import java.net.URL;

import java.net.MalformedURLException;

import java.nio.channels.Channels;

import java.nio.channels.ReadableByteChannel;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.io.FileOutputStream;

import java.io.File;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.net.HttpURLConnection;

import java.util.Random;

/**
 * JGMSFX Class.
 */
public class JGMSFX {
    private static final Random random = new Random();

    private static final CachedSFXBuffer cachedSFX = new CachedSFXBuffer();

    /**
     * JGMSFX Constants.
     */
    public static class Constants {
        /**
         * Sound URL template.
         */
        public static String SOUND_URL = "https://github.com/xzripper/gmsfx3-sounds/blob/main/sounds/%s.wav?raw=true";

        /**
         * Base version URL.
         */
        public static String BASE_VERSION_URL = "https://github.com/xzripper/gmsfx3-sounds/blob/main/sounds/BASE-VERSION?raw=true";

        /**
         * Undefined literal.
         */
        public static String UNDEFINED = "undefined";
    }

    /**
     * Version.
     */
    public static final String JGMSFX_VERSION = "v1.0.1";

    /**
     * Static base version. Use for more fast access to version, but may be older than actual version.
     */
    public static final String BASE_VERSION_STATIC = "13.05.2024";

    /**
     * Generate URL to SFX.
     */
    public static String jgmsfxGenerateUrl(String sfxName) {
        return String.format(Constants.SOUND_URL, sfxName);
    }

    /**
     * Get SFX (Simplified). Returns <code>JGMSFXDownloadResult</code>.
     */
    public static JGMSFXDownloadResult jgmsfxGet(String sfxName) {
        return jgmsfxGet(sfxName, null, false);
    }

    /**
     * Get SFX (returns <code>JGMSFXDownloadResult</code>).
     */
    public static JGMSFXDownloadResult jgmsfxGet(String sfxName, String savePath, boolean randomize) {
        URL sfxUrl;

        try {
            sfxUrl = new URL(jgmsfxGenerateUrl(sfxName));
        } catch(MalformedURLException mException) {
            return new JGMSFXDownloadResult(null, new String[] {"MalformedURLException-URL", "Unknown exception source.", null});
        }

        ReadableByteChannel channel;

        try {
            channel = Channels.newChannel(sfxUrl.openStream());
        } catch(IOException ioException) {
            return new JGMSFXDownloadResult(null, new String[] {"IOException-URL.openStream", "Probably invalid SFX name.", null});
        }

        String sfxPath;

        if(savePath == null) {
            String temporaryDirectory = System.getProperty("java.io.tmpdir");

            sfxPath = String.format(
                randomize ? "%s%s%s_%d.wav" : "%s%s%s%s.wav",

                temporaryDirectory,

                temporaryDirectory.endsWith(System.getProperty("file.separator")) ? "" : System.getProperty("file.separator"),

                sfxName,

                randomize ? random.nextInt(256) : "");
        } else {
            sfxPath = String.format(
                randomize ? "%s%s%s_%d.wav" : "%s%s%s%s.wav",

                savePath,

                savePath.endsWith(System.getProperty("file.separator")) ? "" : System.getProperty("file.separator"),

                sfxName,

                randomize ? random.nextInt(256) : "");
        }

        FileOutputStream output;

        try {
            output = new FileOutputStream(sfxPath);
        } catch(FileNotFoundException notFoundException) {
            return new JGMSFXDownloadResult(null, new String[] {"FileNotFoundException-FileOutputStream", "Unknown exception source.", null});
        }

        try {
            output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);

            output.close();
        } catch(IOException ioException) {
            return new JGMSFXDownloadResult(null, new String[] {"IOException-transferFrom/close", "Unknown exception source.", null});
        }

        cachedSFX.cacheSFX(sfxPath);

        return new JGMSFXDownloadResult(sfxPath, null);
    }

    /**
     * Get sounds base version. Use <code>jgmsfxStaticBaseVersion</code> for faster access to base version.
     */
    public static String jgmsfxBaseVersion() {
        StringBuilder GETOutput = new StringBuilder();

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(Constants.BASE_VERSION_URL).openConnection();

            connection.setRequestMethod("GET");

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                for(String line; (line = reader.readLine()) != null;) {
                    GETOutput.append(line);
                }
            }

            return GETOutput.toString();
        } catch(IOException ioException) {
            return Constants.UNDEFINED;
        }
    }

    /**
     * Clear cached SFX.
     */
    public static void jgmsfxClearCachedSFX() {
        for(String sfxPath : cachedSFX.getBuffer()) {
            new File(sfxPath).delete();

            cachedSFX.removeSFX(sfxPath);
        }
    }

    /**
     * Get cached SFX.
     */
    public static CachedSFXBuffer jgmsfxGetCachedSFXBuffer() {
        return cachedSFX;
    }

    /**
     * Get sounds base static version. Use for more fast access to version, but may be older than actual version.
     */
    public static String jgmsfxStaticBaseVersion() {
        return BASE_VERSION_STATIC;
    }

    /**
     * Get JGMSFX version.
     */
    public static String jgmsfxVersion() {
        return JGMSFX_VERSION;
    }
}
