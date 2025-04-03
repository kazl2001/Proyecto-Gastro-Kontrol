package model.error.item;

import model.error.RestaurantError;

public final class OrderItemDataRetrievalError extends RestaurantError {
    public OrderItemDataRetrievalError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
