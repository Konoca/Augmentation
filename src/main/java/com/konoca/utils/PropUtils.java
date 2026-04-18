package com.konoca.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropUtils {
    private static Logger logger = Logger.getLogger(PropUtils.class.getName());

    public static String getProperty(String propName) {
        try {
            Properties props = new Properties();
            try (InputStream is = PropUtils.class
                    .getClassLoader()
                    .getResourceAsStream("app.properties")) {
                props.load(is);
            }

            // String version = props.getProperty("app.version");
            return props.getProperty(propName);
        } catch (Exception e) {
            logger.severe("Error getting property " + propName);
            e.printStackTrace();
            return "";
        }
    }
}
