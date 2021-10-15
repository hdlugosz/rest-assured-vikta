package vikta.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static ConfigLoader instance = null;
    private final Properties properties;

    private ConfigLoader() {
        properties = new Properties();
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
    }

    public static synchronized ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public String getPropertyValue(String key) {
        return this.properties.getProperty(key);
    }
}
