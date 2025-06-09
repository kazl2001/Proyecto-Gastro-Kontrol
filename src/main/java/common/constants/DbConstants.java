package common.constants;

public class DbConstants {

    public DbConstants() {
    }

    //customers
    public static final String ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";

    //orders
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_DATE = "order_date";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String TABLE_ID = "table_id";

    //order-items
    public static final String ORDER_ITEM_ID = "order_item_id";
    public static final String QUANTITY = "quantity";
    public static final String MENU_ITEM_ID = "menu_item_id";

    //menu-items
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String DESCRIPTION = "description";

    //tables
    public static final String TABLE_NUMBER_ID = "table_number_id";

    //credentials
    public static final String USERNAME = "user_name";
    public static final String PASSWORD = "password";

    //TABLE NAMES
    public static final String CUSTOMERS_TABLE = "customers";
    public static final String ORDERS_TABLE = "orders";
    public static final String ORDER_ITEMS_TABLE = "order_items";
    public static final String MENU_ITEMS_TABLE = "menu_items";
    public static final String TABLES_TABLE = "restaurant_tables";
    public static final String CREDENTIALS_TABLE = "credentials";


}
