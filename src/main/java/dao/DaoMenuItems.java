package dao;

import io.vavr.control.Either;
import model.MenuItem;
import model.error.RestaurantError;

import java.util.ArrayList;

public interface DaoMenuItems {
    Either<RestaurantError, ArrayList<MenuItem>> getAll();
}
