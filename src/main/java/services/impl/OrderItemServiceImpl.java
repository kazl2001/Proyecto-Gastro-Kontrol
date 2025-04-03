package services.impl;

import common.constants.Constants;
import common.constants.ErrorConstants;
import dao.DaoMenuItems;
import dao.DaoOrderItems;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.OrderItem;
import model.error.RestaurantError;
import model.error.item.OrderItemDataValidationError;
import services.OrderItemService;

import java.util.ArrayList;
import java.util.List;

public class OrderItemServiceImpl implements OrderItemService {

    private final DaoMenuItems daoMenuItems;
    private final DaoOrderItems daoOrderItems;

    @Inject
    public OrderItemServiceImpl(DaoMenuItems daoMenuItems, DaoOrderItems daoOrderItems) {
        this.daoMenuItems = daoMenuItems;
        this.daoOrderItems = daoOrderItems;
    }


    @Override
    public Either<RestaurantError, ArrayList<OrderItem>> getAll(int id) {
        if (id <= 0) {
            return Either.left(new OrderItemDataValidationError(ErrorConstants.INVALID_DATA_FORMAT_ERROR_CODE, ErrorConstants.INVALID_DATA_FORMAT_ERROR));
        } else {
            OrderItem orderItem = new OrderItem(id);
            return daoOrderItems.getAll(orderItem).fold(
                    Either::left,
                    Either::right
            );
        }
    }
    @Override
    public Either<RestaurantError, Double> getTotalPrice(List<OrderItem> orderItemList) {
        double totalPrice = 0.0;
        if (orderItemList.isEmpty()) {
            return Either.left(new OrderItemDataValidationError(ErrorConstants.INVALID_DATA_FORMAT_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        }
        List<MenuItem> menuItemList = new ArrayList<>();
        daoMenuItems.getAll().peek(menuItemList::addAll);

        for (OrderItem orderItem : orderItemList) {
            for (MenuItem menuItem : menuItemList) {
                MenuItem item = orderItem.getMenuItem();
                if (item.getId() == menuItem.getId()) {
                    totalPrice += menuItem.getPrice() * orderItem.getQuantity();
                }
            }
        }

        String formattedTotalPrice = String.format(Constants.DECIMAL_FORMAT, totalPrice).replace(Constants.COMMA, Constants.FULL_STOP);

        try {
            return Either.right(Double.parseDouble(formattedTotalPrice));
        } catch (NumberFormatException e) {
            return Either.left(new RestaurantError(ErrorConstants.INVALID_DATA_FORMAT_ERROR_CODE, ErrorConstants.INVALID_DATA_FORMAT_ERROR));
        }
    }

}
