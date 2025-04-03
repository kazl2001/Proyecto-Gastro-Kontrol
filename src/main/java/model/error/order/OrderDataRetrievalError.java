package model.error.order;

import model.error.RestaurantError;

public final class OrderDataRetrievalError extends RestaurantError {
    public OrderDataRetrievalError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
