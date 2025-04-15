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

    //IMAGES CONSTANTS
    public static final String LOGO_IMAGE = "/media/logo.png";
    public static final String USER_IMAGE = "/media/user.png";
    public static final String PASSWORD_IMAGE = "/media/padlock.png";

    public static final String WELCOME_BACKGROUND_IMAGE = "/media/background_w.png";
    public static final String LIST_CUSTOMER_BACKGROUND_IMAGE = "/media/background_lc.png";
    public static final String ADD_CUSTOMER_BACKGROUND_IMAGE = "/media/background_ac.png";
    public static final String UPDATE_CUSTOMER_BACKGROUND_IMAGE = "/media/background_uc.png";
    public static final String DELETE_CUSTOMER_BACKGROUND_IMAGE = "/media/background_dc.png";


    public static final String LIST_ORDER_BACKGROUND_IMAGE = "/media/background_lo.png";
    public static final String ADD_ORDER_BACKGROUND_IMAGE = "/media/background_ao.png";
    public static final String UPDATE_ORDER_BACKGROUND_IMAGE = "/media/background_uo.png";
    public static final String DELETE_ORDER_BACKGROUND_IMAGE = "/media/background_do.png";

}
