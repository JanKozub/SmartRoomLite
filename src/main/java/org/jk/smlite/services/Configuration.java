
package org.jk.smlite.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

@Service
public class Configuration {

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);
    private static final File config = new File(System.getProperty("user.home") + "/.smartroom.properties");

    private final PropertyResolver resolver;

    private volatile Properties properties = new Properties();

    Configuration(@Qualifier("environment") PropertyResolver resolver) {
        this.resolver = resolver;
        readProperties();
    }

    public String readProperty(String path) {
        String value;
        if (properties.containsKey(path))
            value = (String) properties.get(path);
        else
            value = resolver.getProperty(path);

        log.debug("Read property {}={}", path, value);
        return value;
    }

    public void setProperty(String path, String value) {
        log.debug("Wrote property {}={}", path, value);
        String property = (String) properties.get(path);
        if (!Objects.equals(property, value)) {
            if (value == null)
                properties.remove(path);
            else
                properties.put(path, value);
            storeProperties();
        }
    }

    private void storeProperties() {
        try (OutputStream stream = new FileOutputStream(config)) {
            properties.store(stream, "");
            log.info("stored config file");
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private void readProperties() {
        try {
            if (!config.exists()) {
                Files.write(config.toPath(), new byte[0]);
            }

            Properties newProperties = new Properties();
            try (FileInputStream stream = new FileInputStream(config)) {
                newProperties.load(stream);
            }
            properties = newProperties;
            log.info("loaded config file");
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
