package dao.impl.jdbc_spring;

import common.constants.DbConstants;

public class QueryStrings {

    private QueryStrings() {
    }

    //CUSTOMER DAO
    public static final String GET_ALL_CUSTOMERS = "SELECT * FROM customers";
    public static final String GET_CUSTOMER_BY_ID = "SELECT * FROM customers where id = ?";
    public static final String INSERT_CUSTOMER = "INSERT INTO customers (first_name, last_name, date_of_birth, email, phone) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_CUSTOMER = "UPDATE customers SET id = ?, first_name = ?, last_name = ?, date_of_birth = ?, email = ?, phone = ? WHERE id = ?";
    public static final String DELETE_CUSTOMER = "DELETE FROM customers WHERE id = ?";
    public static final String DELETE_ORDER_BY_CUSTOMER_ID = "DELETE FROM orders WHERE customer_id = ?";
    public static final String DELETE_ORDER_ITEM_BY_CUSTOMER_ID = "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE customer_id = ?)";
    public static final String DELETE_CREDENTIALS_BY_CUSTOMER_ID = "DELETE FROM credentials WHERE customer_id = ?";
    public static final String CHECK_CREDENTIALS = "SELECT COUNT(*) FROM " + DbConstants.CREDENTIALS_TABLE + " WHERE " + DbConstants.USERNAME + " = ?";

    //ORDER DAO
    public static final String GET_ALL_ORDERS = "SELECT * FROM orders";
    public static final String GET_ALL_ORDERS_BY_CUSTOMER_ID = "SELECT * FROM orders where customer_id = ?";
    public static final String GET_ORDER_BY_ID = "SELECT * FROM orders where order_id = ?";
    public static final String INSERT_ORDER = "INSERT INTO orders (table_id, customer_id, order_date) VALUES (?, ?, ?)";
    public static final String UPDATE_ORDER = "UPDATE orders SET table_id = ?, customer_id = ?, order_date = ? WHERE order_id = ?";
    public static final String DELETE_ORDER = "DELETE FROM orders WHERE order_id = ?";
    public static final String DELETE_ORDER_ITEMS_BY_ORDER_ID = "DELETE FROM order_items WHERE order_id = ?";


    //ORDER-ITEM DAO
    public static final String GET_ALL_BY_ORDER_ID = "SELECT * FROM order_items where order_id = ?";
    public static final String INSERT_ORDER_ITEM = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";


    //MENU-ITEM DAO
    public static final String GET_ALL_MENU_ITEMS = "SELECT * FROM menu_items";


    //TABLE DAO
    public static final String GET_ALL_TABLES = "SELECT * FROM restaurant_tables";


    //CREDENTIAL DAO
    public static final String GET_ALL_CREDENTIALS = "SELECT * FROM credentials";
    public static final String INSERT_CREDENTIAL = "INSERT INTO credentials (customer_id,user_name, password) VALUES (?, ?, ?)";
    public static final String GET_CREDENTIAL_BY_ID = "SELECT * FROM credentials where customer_id = ?";


    //DAO ORDER SPRING
    public static final String GET_ALL_ORDERS_SPRING = "SELECT * FROM orders;";
    public static final String GET_ALL_ORDERS_BY_CUSTOMER_ID_SPRING = "SELECT * FROM orders where customer_id = ?;";


    //DAO ORDER ITEM SPRING

    //get all orderItems with their associated menuItems
    public static final String GET_ALL_ORDER_ITEMS_BY_ORDER_ID_SPRING = "SELECT * FROM order_items inner join menu_items on order_items.menu_item_id = menu_items.menu_item_id where order_items.order_id = ?";

    //get a specific orderItem with its associated menuItem
    public static final String GET_ORDER_ITEM_BY_ID_SPRING = "SELECT * FROM order_items inner join menu_items on order_items.menu_item_id = menu_items.menu_item_id where order_items.order_item_id = ?";

    // CHECK THE HASHED CREDENTIALS
     static final String GET_CREDENTIAL_BY_USERNAME = "SELECT * FROM credentials WHERE user_name = ?";

}
