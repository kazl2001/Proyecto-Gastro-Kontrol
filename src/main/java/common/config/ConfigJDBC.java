package common.config;

import common.constants.Constants;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Singleton
@Getter
@Log4j2
public class ConfigJDBC {

    private final Properties propertiesJDBC;

    private ConfigJDBC() {
        Path p1 = Paths.get(Constants.MYSQL_CONFIG_PATH);
        propertiesJDBC = new Properties();
        InputStream propertiesStream;

        try {
            propertiesStream = Files.newInputStream(p1);
            propertiesJDBC.loadFromXML(propertiesStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public String getProperty(String key) {
        return propertiesJDBC.getProperty(key);
    }

}
