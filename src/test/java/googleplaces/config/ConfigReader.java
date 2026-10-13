package googleplaces.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Loads test configuration from the classpath, independent of operating system. */
public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("global.properties")) {
            if (input == null) {
                throw new IllegalStateException("global.properties was not found on the test classpath.");
            }
            PROPERTIES.load(input);
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            value = PROPERTIES.getProperty(key);
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required configuration property: " + key);
        }
        return value.trim();
    }
}
