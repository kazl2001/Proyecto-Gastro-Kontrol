package model.error.customer;

import model.error.RestaurantError;

public final class CustomerDataRetrievalError extends RestaurantError {
    public CustomerDataRetrievalError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
