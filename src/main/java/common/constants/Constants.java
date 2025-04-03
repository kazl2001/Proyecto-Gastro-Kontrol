package common.constants;

public class Constants {

    private Constants() {
    }

    //DB CONFIG CONNECTION POOL STRINGS
    public static final String JDBC_URL = "urlDB";
    public static final String USERNAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String DRIVER = "driver";
    public static final String CACHE_PREP_STATEMENTS = "cachePrepStmts";
    public static final String PREP_STMT_CACHE_SIZE = "prepStmtCacheSize";
    public static final String PREP_STMT_CACHE_SQL_LIMIT = "prepStmtCacheSqlLimit";

    //CONFIG STRINGS
    public static final String XML_SETTINGS_FILE_PATH = "config/settings.xml";
    public static final String PATH_DATA_FOLDER = "pathDataFolder";
    public static final String MYSQL_CONFIG_PATH = "src/main/resources/config/mysql.xml";

    //MODEL STRINGS
    public static final String SPACE = " ";
    public static final String HYPHEN = "-";
    public static final String EMPTY_STRING = "";

    public static final String TABLE = "Table";
    public static final String SEATS = "seats";
    public static final String ORDER = "order";
    public static final String ORDER_ITEM = "order_item";

    //SERVICES STRINGS
    public static final String DECIMAL_FORMAT = "%.2f";
    public static final char FULL_STOP = '.';
    public static final char COMMA = ',';

    //DAO XML
    public static final String TEXT_FORMAT = "Orders-customer-%s.xml";

}
