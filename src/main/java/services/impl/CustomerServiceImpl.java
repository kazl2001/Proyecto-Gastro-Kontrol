package services.impl;

import common.constants.Constants;
import common.constants.ErrorConstants;
import dao.DaoCustomers;
import dao.DaoOrderItems;
import dao.DaoOrders;
import dao.impl.xml.DaoOrdersXMLImpl;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.error.RestaurantError;
import model.error.customer.CustomerValidationError;
import services.CustomerService;

import java.util.ArrayList;

public class CustomerServiceImpl implements CustomerService {
    private final DaoCustomers daoSpring;
    private final DaoOrdersXMLImpl daoOrdersXML;
    private final DaoOrders daoOrders;

    private final DaoOrderItems daoOrderItems;

    @Inject
    public CustomerServiceImpl(DaoCustomers daoSpring, DaoOrdersXMLImpl daoOrdersXML, DaoOrders daoOrders, DaoOrderItems daoOrderItems) {
        this.daoSpring = daoSpring;
        this.daoOrdersXML = daoOrdersXML;
        this.daoOrders = daoOrders;
        this.daoOrderItems = daoOrderItems;
    }

    @Override
    public Either<RestaurantError, ArrayList<Customer>> getAll() {
        return daoSpring.getAll();
    }

    @Override
    public Either<RestaurantError, Customer> get(int id) {
        Customer c = new Customer(id);
        return daoSpring.get(c);
    }

    @Override
    public Either<RestaurantError, Integer> save(Customer customer) {
        if (customer != null) {
            return daoSpring.save(customer);
        } else {
            return Either.left(new CustomerValidationError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, Integer> delete(Customer customer, Boolean confirmed) {
        Either<RestaurantError, Integer> result;

        if (customer == null) {
            result = Either.left(new CustomerValidationError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        } else {
            Order order = new Order(customer.getId());
            Either<RestaurantError, ArrayList<Order>> orders = daoOrders.getAll(order);
            if (orders.isRight()) {
                ArrayList<Order> ordersList = orders.get();
                for (Order o : ordersList) {
                    ArrayList<OrderItem> orderItems = daoOrderItems.getAll(new OrderItem(o.getId())).get();
                    if (!orderItems.isEmpty()) {
                        o.setOrderItems(orderItems);
                    }
                }

                daoOrdersXML.save(orders.get());
            }
            result = daoSpring.delete(customer, confirmed);
        }
        return result;
    }

    @Override
    public Either<RestaurantError, Integer> delete(Customer customer) {
        Either<RestaurantError, Integer> result;

        if (customer == null) {
            result = Either.left(new CustomerValidationError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        } else {
            Order order = new Order(customer.getId());
            ArrayList<Order> orders = daoOrders.getAll(order).get();
            if (!orders.isEmpty()) {
                for (Order o : orders) {
                    ArrayList<OrderItem> orderItems = daoOrderItems.getAll(new OrderItem(o.getId())).get();
                    if (!orderItems.isEmpty()) {
                        o.setOrderItems(orderItems);
                    }
                }

                daoOrdersXML.save(orders);
            }
            result = daoSpring.delete(customer);
        }
        return result;
    }

    @Override
    public Either<RestaurantError, Integer> update(Customer newCustomer) {
        if (newCustomer == null) {
            return Either.left(new CustomerValidationError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        } else {
            return daoSpring.update(newCustomer);
        }
    }
}
