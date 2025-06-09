package ui.screens.common;

public class ScreenConstants {

    private ScreenConstants() {
    }

    //SCREEN ROUTES
    public static final String FXML_ROUTE_PRINCIPAL = "/fxml/principal.fxml";
    public static final String FXML_ROUTE_LOGIN = "/fxml/login.fxml";
    public static final String FXML_ROUTE_CUSTOMER_LIST_SCREEN = "/fxml/customerList.fxml";
    public static final String FXML_ROUTE_CUSTOMER_ADD_SCREEN = "/fxml/customerAdd.fxml";
    public static final String FXML_ROUTE_CUSTOMER_UPDATE_SCREEN = "/fxml/customerUpdate.fxml";
    public static final String FXML_ROUTE_CUSTOMER_DELETE_SCREEN = "/fxml/customerDelete.fxml";
    public static final String FXML_ROUTE_WELCOME_SCREEN = "/fxml/welcome.fxml";
    public static final String FXML_ROUTE_ORDER_LIST_SCREEN = "/fxml/orderList.fxml";
    public static final String FXML_ROUTE_ORDER_ADD_SCREEN = "/fxml/orderAdd.fxml";
    public static final String FXML_ROUTE_ORDER_UPDATE_SCREEN = "/fxml/orderUpdate.fxml";
    public static final String FXML_ROUTE_ORDER_DELETE_SCREEN = "/fxml/orderDelete.fxml";

    //WELCOME TXT AREA
    public static final String EXCLAMATION_MARK = " !";

    //ALERT ID'S
    public static final String ALERT_DIALOG_PANE_ID = "Alert";
    public static final String ALERT_CONFIRMATION_BUTTON_ID = "ConfirmationButton";

    //ALERT MESSAGES
    public static final String SUCCESSFUL_ACTION = "Action performed successfully. ";
    public static final String ACTION_NOT_COMPLETED = "The action could not be completed.";
    public static final String EMPTY_TABLE = "EMPTY TABLE";
    public static final String NO_CHANGES_MADE = "No changes were made. Please modify something first";
    public static final String CUSTOMER_MODIFIED = "Customer modified.";
    public static final String ORDER_ADDED = "Order added.";
    public static final Object ORDER_NOT_ADDED = "The order could not be added.";
    public static final String ORDER_ITEM_NOT_ADDED = "The order item could not be added.";
    public static final String ORDER_ITEM_ADDED = "Order item added.";
    public static final String ORDER_ITEM_REMOVED = "Order item removed.";
    public static final String ORDER_ITEM_NOT_REMOVED = "The order item could not be removed.";
    public static final String ORDER_MODIFIED = "Order modified.";
    public static final String ORDER_DELETED = "Order deleted.";
    public static final String CUSTOMER_DELETED = "Customer deleted.";
    public static final String MISSING_FIELDS = "There are missing fields.";
    public static final String EMPTY_ORDER = "The order is empty. Double click on the order on the table first.";
    public static final String CUSTOMER_HAS_ORDERS_DELETION_CONFIRMATION = "The customer has orders. Are you sure you want to delete it?";
    public static final String OK = "OK";

    //APPLICATION TITLE
    public static final String APP_TITLE = "app.title";
    public static final String APP_TITLE_PROPERTY_ROUTE = "/i18n/appStrings";

    //TOP MENU ITEM ID'S

    public static final String MENU_ITEM_LOGOUT = "menuItemLogout";
    public static final String MENU_ITEM_EXIT = "menuItemExit";
    public static final String MENU_ITEM_CUSTOMER_LIST = "menuItemCustomerList";
    public static final String MENU_ITEM_CUSTOMER_ADD = "menuItemCustomerAdd";
    public static final String MENU_ITEM_CUSTOMER_UPDATE = "menuItemCustomerUpdate";
    public static final String MENU_ITEM_CUSTOMER_DELETE = "menuItemCustomerDelete";
    public static final String MENU_ITEM_ORDER_LIST = "menuItemOrderList";
    public static final String MENU_ITEM_ORDER_ADD = "menuItemOrderAdd";
    public static final String MENU_ITEM_ORDER_UPDATE = "menuItemOrderUpdate";
    public static final String MENU_ITEM_ORDER_DELETE = "menuItemOrderDelete";

    //TABLE CREATION
    public static final String TABLE_COLUMN_FIRST_NAME = "FIRST NAME";
    public static final String TABLE_COLUMN_LAST_NAME = "LAST NAME";
    public static final String TABLE_COLUMN_DOB = "DATE OF BIRTH";
    public static final String TABLE_COLUMN_EMAIL = "EMAIL";
    public static final String TABLE_COLUMN_PHONE = "PHONE";
    public static final String TABLE_COLUMN_MENU_ITEM = "MENU ITEM";
    public static final String TABLE_COLUMN_QUANTITY = "QUANTITY";
    public static final String TABLE_COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String TABLE_COLUMN_PRICE = "PRICE";
    public static final String TABLE_COLUMN_TABLE = "TABLE";
    public static final String TABLE_COLUMN_ID = "ID";
    public static final String TABLE_COLUMN_DATE = "DATE";
}
