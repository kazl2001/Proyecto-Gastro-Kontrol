package dao;

import io.vavr.control.Either;
import model.Credential;
import model.error.RestaurantError;

import java.util.ArrayList;

public interface DaoCredentials {
    Either<RestaurantError, ArrayList<Credential>> getAll();

    Either<RestaurantError, Credential> get(Credential credential);

    Either<RestaurantError, Integer> save(Credential credential);

}
