package model.error.database;

import model.error.RestaurantError;

public final class FileNotFoundError extends RestaurantError {
    public FileNotFoundError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
