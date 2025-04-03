package dao;

import io.vavr.control.Either;
import model.Customer;
import model.error.RestaurantError;

import java.util.ArrayList;

public interface DaoCustomers {
    Either<RestaurantError, ArrayList<Customer>> getAll();

    Either<RestaurantError, Customer> get(Customer customer);

    Either<RestaurantError, Integer> update(Customer newCustomer);

    Either<RestaurantError, Integer> delete(Customer customer);

    Either<RestaurantError, Integer> delete(Customer customer, boolean confirmed);

    Either<RestaurantError, Integer> save(Customer newCustomer);
}
