package model.error.database;

import model.error.RestaurantError;

public final class ConnectionFailedError extends RestaurantError {
    public ConnectionFailedError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
