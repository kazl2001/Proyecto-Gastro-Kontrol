package model.error.customer;

import model.error.RestaurantError;

public final class ConfirmationNeededError extends RestaurantError {
    public ConfirmationNeededError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
