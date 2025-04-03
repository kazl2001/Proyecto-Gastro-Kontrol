package dao.impl.jdbc_spring;

import common.constants.DbConstants;
import common.constants.ErrorConstants;
import dao.DaoCustomers;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Credential;
import model.Customer;
import model.error.RestaurantError;
import model.error.customer.ConfirmationNeededError;
import model.error.customer.CustomerDataRetrievalError;
import model.error.database.GeneralDatabaseError;
import model.error.database.QueryExecutionError;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class DaoCustomersImpl implements DaoCustomers {

    private final DBConnectionPool pool;

    @Inject
    public DaoCustomersImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<RestaurantError, ArrayList<Customer>> getAll() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            ArrayList<Customer> customers = (ArrayList<Customer>) jdbcTemplate.query(QueryStrings.GET_ALL_CUSTOMERS, BeanPropertyRowMapper.newInstance(Customer.class));
            customers = customers.stream()
                    .filter(customer -> customer.getId() != -1)
                    .collect(Collectors.toCollection(ArrayList::new));
            return Either.right(customers);
        } catch (Exception e) {
            return Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, Customer> get(Customer customer) {
        Either<RestaurantError, Customer> result;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            int id = customer.getId();
            List<Customer> customers = jdbcTemplate.query(QueryStrings.GET_CUSTOMER_BY_ID, BeanPropertyRowMapper.newInstance(Customer.class), id);
            result = Either.right(customers.get(0));
        } catch (DataAccessException e) {
            result = Either.left(new CustomerDataRetrievalError(ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND_ERROR_CODE, ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        }
        return result;
    }

    @Override
    public Either<RestaurantError, Integer> update(Customer newCustomer) {
        Customer customer = checkEmptyItemFields(newCustomer);
        Either<RestaurantError, Integer> result;
        try {
            Date dateOfBirth;

            if (customer.getDateOfBirth() != null) {
                dateOfBirth = Date.valueOf(customer.getDateOfBirth());
            } else {
                dateOfBirth = null;
            }

            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            result = Either.right(jdbcTemplate.update(
                    QueryStrings.UPDATE_CUSTOMER,
                    customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    dateOfBirth,
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getId()));
        } catch (Exception e) {
            result = Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    @Override
    public Either<RestaurantError, Integer> delete(Customer customer) {
        Either<RestaurantError, Integer> result;

        TransactionDefinition def = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(pool.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(transactionManager.getDataSource()));

            jdbcTemplate.update(QueryStrings.DELETE_ORDER_ITEM_BY_CUSTOMER_ID, customer.getId());
            jdbcTemplate.update(QueryStrings.DELETE_ORDER_BY_CUSTOMER_ID, customer.getId());
            jdbcTemplate.update(QueryStrings.DELETE_CUSTOMER, customer.getId());
            result = Either.right(jdbcTemplate.update(QueryStrings.DELETE_CREDENTIALS_BY_CUSTOMER_ID, customer.getId()));
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            result = Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    @Override
    public Either<RestaurantError, Integer> delete(Customer customer, boolean confirmed) {

        Either<RestaurantError, Integer> result;

        TransactionDefinition def = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(pool.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(transactionManager.getDataSource()));

            if (!confirmed) {
                result = deleteCustomerAndCredentialsOnly(jdbcTemplate, customer, transactionManager, status);
            } else {
                jdbcTemplate.update(QueryStrings.DELETE_ORDER_ITEM_BY_CUSTOMER_ID, customer.getId());
                jdbcTemplate.update(QueryStrings.DELETE_ORDER_BY_CUSTOMER_ID, customer.getId());
                jdbcTemplate.update(QueryStrings.DELETE_CUSTOMER, customer.getId());
                result = Either.right(jdbcTemplate.update(QueryStrings.DELETE_CREDENTIALS_BY_CUSTOMER_ID, customer.getId()));
                transactionManager.commit(status);
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            result = Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    private Either<RestaurantError, Integer> deleteCustomerAndCredentialsOnly(JdbcTemplate jdbcTemplate, Customer customer, DataSourceTransactionManager transactionManager, TransactionStatus status) {
        Either<RestaurantError, Integer> result;
        try {
            jdbcTemplate.update(QueryStrings.DELETE_CUSTOMER, customer.getId());
            result = Either.right(jdbcTemplate.update(QueryStrings.DELETE_CREDENTIALS_BY_CUSTOMER_ID, customer.getId()));
            transactionManager.commit(status);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            transactionManager.rollback(status);
            result = Either.left(new ConfirmationNeededError(ErrorConstants.CONFIRMATION_NEEDED_ERROR_CODE, ErrorConstants.CONFIRMATION_NEEDED_ERROR));
        }
        return result;
    }

    @Override
    public Either<RestaurantError, Integer> save(Customer newCustomer) {
        Customer customer = checkEmptyItemFields(newCustomer);
        Either<RestaurantError, Integer> result;

        TransactionDefinition def = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(pool.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            // 1. Insertar en la tabla customers primero
            SimpleJdbcInsert customerInsert = new SimpleJdbcInsert(pool.getDataSource())
                    .withTableName(DbConstants.CUSTOMERS_TABLE)
                    .usingGeneratedKeyColumns("id"); // se asume que la columna primaria es "id"
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put(DbConstants.FIRST_NAME, customer.getFirstName());
            customerParams.put(DbConstants.LAST_NAME, customer.getLastName());
            customerParams.put(DbConstants.EMAIL, customer.getEmail());
            customerParams.put(DbConstants.PHONE, customer.getPhone());
            if (customer.getDateOfBirth() != null) {
                customerParams.put(DbConstants.DATE_OF_BIRTH, Date.valueOf(customer.getDateOfBirth()));
            } else {
                customerParams.put(DbConstants.DATE_OF_BIRTH, null);
            }
            // Obtener el id generado en customers
            int generatedCustomerId = customerInsert.executeAndReturnKey(customerParams).intValue();

            // 2. Insertar en la tabla credentials usando el customer_id obtenido
            SimpleJdbcInsert credentialInsert = new SimpleJdbcInsert(pool.getDataSource())
                    .withTableName(DbConstants.CREDENTIALS_TABLE);
            Map<String, Object> credParams = new HashMap<>();
            // Nota: La columna en la tabla credentials se llama "customer_id" según las constantes
            credParams.put("customer_id", generatedCustomerId);
            credParams.put(DbConstants.USERNAME, customer.getCredential().getUsername());
            credParams.put(DbConstants.PASSWORD, customer.getCredential().getPassword());
            int affectedRows = credentialInsert.execute(credParams);
            if (affectedRows == 0) {
                transactionManager.rollback(status);
                return Either.left(new GeneralDatabaseError(ErrorConstants.NO_ROWS_AFFECTED_ERROR_CODE, "No se insertó la credencial"));
            }

            transactionManager.commit(status);
            result = Either.right(generatedCustomerId);
        } catch (Exception e) {
            transactionManager.rollback(status);
            result = Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, "Error al insertar cliente: " + e.getMessage()));
        }
        return result;
    }

    /* SAVE ANTIGUO
    @Override
    public Either<RestaurantError, Integer> save(Customer newCustomer) {
        Customer customer = checkEmptyItemFields(newCustomer);
        Either<RestaurantError, Integer> result;


        TransactionDefinition def = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(pool.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(def);

        Map<String, Object> params;

        try {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(pool.getDataSource())
                    .withTableName(DbConstants.CREDENTIALS_TABLE)
                    .usingColumns(DbConstants.USERNAME, DbConstants.PASSWORD)
                    .usingGeneratedKeyColumns(DbConstants.CUSTOMER_ID);

            params = new HashMap<>();

            Credential credential = customer.getCredential();

            params.put("user_name", credential.getUsername());
            params.put("password", credential.getPassword());

            int generatedKey;

            Either<RestaurantError, Integer> credentialInsertResult = insertCredential(status, transactionManager, insert, params);

            if (credentialInsertResult.isRight()) {
                generatedKey = credentialInsertResult.get();
            } else {
                return credentialInsertResult;
            }

            if (generatedKey == 0) {
                transactionManager.rollback(status);
                return Either.left(new GeneralDatabaseError(ErrorConstants.NO_GENERATED_KEY_ERROR_CODE, ErrorConstants.NO_GENERATED_KEY));
            } else {

                insert = new SimpleJdbcInsert(pool.getDataSource())
                        .withTableName(DbConstants.CUSTOMERS_TABLE)
                        .usingColumns(DbConstants.ID, DbConstants.FIRST_NAME, DbConstants.LAST_NAME, DbConstants.EMAIL, DbConstants.PHONE, DbConstants.DATE_OF_BIRTH);

                params = new HashMap<>();

                params.put(DbConstants.ID, generatedKey);
                params.put(DbConstants.FIRST_NAME, customer.getFirstName());
                params.put(DbConstants.LAST_NAME, customer.getLastName());
                params.put(DbConstants.EMAIL, customer.getEmail());
                params.put(DbConstants.PHONE, customer.getPhone());
                if (customer.getDateOfBirth() != null) {
                    params.put(DbConstants.DATE_OF_BIRTH, Date.valueOf(customer.getDateOfBirth()));
                } else {
                    params.put(DbConstants.DATE_OF_BIRTH, null);
                }

                int affectedRows = insert.execute(params);
                if (affectedRows == 0) {
                    transactionManager.rollback(status);
                    result = Either.left(new GeneralDatabaseError(ErrorConstants.NO_ROWS_AFFECTED_ERROR_CODE, ErrorConstants.NO_ROWS_AFFECTED));
                } else{
                    transactionManager.commit(status);
                    result = Either.right(generatedKey);
                }
            }
        } catch (Exception e) {
            result = Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }
    */

    private Either<RestaurantError, Integer> insertCredential(TransactionStatus status, DataSourceTransactionManager transactionManager, SimpleJdbcInsert insert, Map<String, Object> params) {
        int generatedKey;
        try {
            generatedKey = insert.executeAndReturnKey(params).intValue();
            return Either.right(generatedKey);
        } catch (DuplicateKeyException e) {
            transactionManager.rollback(status);
            return Either.left(new QueryExecutionError(ErrorConstants.UNIQUE_FIELD_CONSTRAINT_ERROR_CODE, ErrorConstants.UNIQUE_FIELD_CONSTRAINT_ERROR));
        }
    }

    private Customer checkEmptyItemFields(Customer customer) {
        if (customer.getPhone().isEmpty() || customer.getPhone().isBlank()) {
            customer.setPhone(null);
        }
        if (Objects.equals(customer.getDateOfBirth(), LocalDate.now())) {
            customer.setDateOfBirth(null);
        }
        return customer;
    }
}
