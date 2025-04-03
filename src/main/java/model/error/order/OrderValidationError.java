package model.error.order;

import model.error.RestaurantError;

public final class OrderValidationError extends RestaurantError {
    public OrderValidationError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
