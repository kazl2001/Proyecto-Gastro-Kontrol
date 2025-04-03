package model.error.customer;

import model.error.RestaurantError;

public final class CustomerValidationError extends RestaurantError {
    public CustomerValidationError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
