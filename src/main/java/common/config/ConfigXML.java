package common.config;

import common.constants.Constants;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Properties;

@Singleton
@Getter
@Log4j2
public class ConfigXML {

    private Properties propertiesXML;
    private String pathDataFolder;

    private ConfigXML() {
        try {
            propertiesXML = new Properties();
            propertiesXML.loadFromXML(ConfigXML.class.getClassLoader().getResourceAsStream(Constants.XML_SETTINGS_FILE_PATH));
            this.pathDataFolder = propertiesXML.getProperty(Constants.PATH_DATA_FOLDER);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
