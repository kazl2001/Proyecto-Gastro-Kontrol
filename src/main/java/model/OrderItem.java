package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {

    private int id;
    private int orderId;
    private MenuItem menuItem;
    private int menuItemId;
    private int quantity;

    public OrderItem(int orderId, int menuItemId, int quantity) {
        this.id = 0;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public OrderItem(int id, int orderId, int menuItemId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public OrderItem( int orderId, MenuItem menuItem, int quantity) {
        this.id = 0;
        this.orderId = orderId;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public OrderItem(int orderId) {
        this.orderId = orderId;
    }
}
