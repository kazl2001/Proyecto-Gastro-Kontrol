package services;

import io.vavr.control.Either;
import model.error.RestaurantError;

public interface LoginService {
    Either<RestaurantError, Integer> login(String user, String pass);

    boolean checkAdmin(String user, String pass);
}
