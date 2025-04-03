package model.error.item;

import model.error.RestaurantError;

public final class OrderItemDataValidationError extends RestaurantError {
    public OrderItemDataValidationError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
