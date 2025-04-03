package model.error.database;

import model.error.RestaurantError;

public final class GeneralDatabaseError extends RestaurantError {
    public GeneralDatabaseError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
