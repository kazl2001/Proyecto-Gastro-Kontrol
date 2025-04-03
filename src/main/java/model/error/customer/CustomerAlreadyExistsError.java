package model.error.customer;

import model.error.RestaurantError;

public final class CustomerAlreadyExistsError extends RestaurantError {
    public CustomerAlreadyExistsError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
