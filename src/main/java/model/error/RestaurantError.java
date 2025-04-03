package model.error;

import lombok.Getter;
import model.error.customer.ConfirmationNeededError;
import model.error.customer.CustomerAlreadyExistsError;
import model.error.customer.CustomerDataRetrievalError;
import model.error.customer.CustomerValidationError;
import model.error.database.ConnectionFailedError;
import model.error.database.FileNotFoundError;
import model.error.database.GeneralDatabaseError;
import model.error.database.QueryExecutionError;
import model.error.order.OrderAlreadyExistsError;
import model.error.order.OrderDataRetrievalError;
import model.error.order.OrderValidationError;
import model.error.item.OrderItemDataRetrievalError;
import model.error.item.OrderItemDataValidationError;

import java.time.LocalDateTime;

@Getter
public sealed class RestaurantError permits ConfirmationNeededError, CustomerAlreadyExistsError, CustomerDataRetrievalError, CustomerValidationError, ConnectionFailedError, FileNotFoundError, GeneralDatabaseError, QueryExecutionError, OrderItemDataRetrievalError, OrderItemDataValidationError, OrderAlreadyExistsError, OrderDataRetrievalError, OrderValidationError {

    private final int errorNum;
    private final String errorMessage;
    private final LocalDateTime errorDate;

    public RestaurantError(int errorNum, String errorMessage) {
        this.errorNum = errorNum;
        this.errorMessage = errorMessage;
        this.errorDate = LocalDateTime.now();
    }
}
