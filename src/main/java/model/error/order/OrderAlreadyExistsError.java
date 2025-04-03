package model.error.order;

import model.error.RestaurantError;

public final class OrderAlreadyExistsError extends RestaurantError {
    public OrderAlreadyExistsError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
