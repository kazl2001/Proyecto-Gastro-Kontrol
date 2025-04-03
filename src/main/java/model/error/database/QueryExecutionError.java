package model.error.database;

import model.error.RestaurantError;

public final class QueryExecutionError extends RestaurantError {
    public QueryExecutionError(int errorNum, String errorMessage) {
        super(errorNum, errorMessage);
    }
}
