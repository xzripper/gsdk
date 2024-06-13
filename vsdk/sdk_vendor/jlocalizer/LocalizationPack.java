// JLocalizer by Violent Studio.

package vsdk.sdk_vendor.jlocalizer;

import java.io.BufferedReader;

import java.io.FileReader;

import java.io.IOException;

import java.util.HashMap;

/**
 * Localization pack.
 */
public class LocalizationPack {
    protected HashMap<String, String> localization = new HashMap<>();

    /**
     * Initialize localization pack parser.
     */
    public LocalizationPack(String pack) throws Exception {
        try(BufferedReader reader = new BufferedReader(new FileReader(pack))) {
            String line = reader.readLine();

            while(line != null) {
                String[] colonSplit_ = line.split(":");

                if(colonSplit_.length < 3) {
                    throw new Exception("colonSplit_.length < 3 == true: Syntax error: Expected 3 elements.");
                }

                localization.put(String.format("%s_%s", colonSplit_[1], colonSplit_[0]), colonSplit_[2]);

                line = reader.readLine();
            }
        } catch(IOException ioException) {
            localization.put("-1", null);
        }
    }

    /**
     * Get localization map.
     */
    public HashMap<String, String> getMap() {
        return localization;
    }

    /**
     * Is pack loaded successfully.
     */
    public boolean isLoaded() {
        return !localization.containsKey("-1");
    }
}
