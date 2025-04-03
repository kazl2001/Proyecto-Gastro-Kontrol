package services;

import io.vavr.control.Either;
import model.Customer;
import model.error.RestaurantError;

import java.util.ArrayList;

public interface CustomerService {
    Either<RestaurantError, ArrayList<Customer>> getAll();

    Either<RestaurantError, Customer> get(int id);

    Either<RestaurantError, Integer> save(Customer customer);

    Either<RestaurantError, Integer> delete(Customer customer,Boolean confirmed);
    Either<RestaurantError, Integer> delete(Customer customer);

    Either<RestaurantError, Integer> update(Customer newCustomer);
}
