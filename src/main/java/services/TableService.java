package services;

import io.vavr.control.Either;
import model.Table;
import model.error.RestaurantError;

import java.util.ArrayList;

public interface TableService {
    Either<RestaurantError, ArrayList<Table>> getAll();
}
