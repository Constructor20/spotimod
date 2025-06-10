package spoti.config;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class SpotiConfig {

    private static final Preferences prefs = Preferences.userNodeForPackage(SpotiConfig.class);
    private static final Map<String, Boolean> config = new HashMap<String, Boolean>();


    public static void loadConfig() {
        for (String key : getAllDefaultKeys()) {
            boolean value = prefs.getBoolean(key, true); // true = valeur par défaut
            config.put(key, value);
        }
    }

    public static void updateOption(String key, boolean value) {
        config.put(key, value);
        prefs.putBoolean(key, value);
    }

    public static boolean getOption(String key) {
        return config.getOrDefault(key, true);
    }

    public static void saveConfig() {
        // Les changements sont déjà sauvegardés dans updateOption
    }

    private static String[] getAllDefaultKeys() {
        return new String[]{
                "Play", "Back", "Skip", "Loop", "Random", "SongIncoming", "VolumeBar", "ProgressBar", "Cover"
        };
    }

    public static void saveInt(String key, int value) {
        prefs.putInt(key, value);
    }


    public static int loadInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

}
