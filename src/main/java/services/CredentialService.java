package services;

import io.vavr.control.Either;
import model.Credential;
import model.Customer;
import model.error.RestaurantError;

import java.util.ArrayList;

public interface CredentialService {
    Either<RestaurantError, ArrayList<Credential>> getAll();

    Either<RestaurantError, Credential> get(int id);

    Either<RestaurantError, Integer> save(Credential credential);

}
