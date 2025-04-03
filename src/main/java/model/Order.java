package model;

import common.constants.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {

    private int id;
    private int tableId;
    private int customerId;
    private LocalDateTime orderDate;
    private List<OrderItem> orderItems;

    public Order(int tableId, int customerId, LocalDateTime orderDate) {
        this.id = 0;
        this.tableId = tableId;
        this.customerId = customerId;
        this.orderDate = orderDate;
    }

    public Order(int id, int tableId, int customerId, LocalDateTime orderDate, List<OrderItem> orderItems) {
        this.id = id;
        this.tableId = tableId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
    }

    public Order(int id, int tableId, int customerId, LocalDateTime orderDate) {
        this.id = id;
        this.tableId = tableId;
        this.customerId = customerId;
        this.orderDate = orderDate;
    }

    public Order(int id) {
        this.id = id;
    }

}
