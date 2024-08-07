// JLocalizer by Violent Studio.

package vsdk.sdk_vendor.jlocalizer;

/**
 * Localization class.
 */
public class Localization {
    private final LocalizationPack locPack;

    private String defaultLanguage = null;

    /**
     * Initialize localization manager.
     */
    public Localization(String locPackPath) {
        locPack = new LocalizationPack(locPackPath);
    }

    /**
     * Is pack loaded successfully.
     */
    public boolean isLoaded() {
        return locPack.isLoaded();
    }

    /**
     * Set language by default.
     */
    public void setDefaultLanguage(String defaultLanguage_) {
        defaultLanguage = defaultLanguage_;
    }

    /**
     * Get default language.
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Get localized string.
     */
    public String getString(String name, String language) {
        return locPack.getMap().get(String.format("%s_%s", name, language));
    }

    /**
     * Get localized string.
     */
    public String getString(String name) {
        if(defaultLanguage != null) {
            return getString(name, defaultLanguage);
        }

        return null;
    }
}
