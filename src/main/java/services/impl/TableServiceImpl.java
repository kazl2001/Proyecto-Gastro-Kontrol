package services.impl;

import dao.DaoTables;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Table;
import model.error.RestaurantError;
import services.TableService;

import java.util.ArrayList;

public class TableServiceImpl implements TableService {

    private final DaoTables dao;

    @Inject
    public TableServiceImpl(DaoTables dao) {
        this.dao = dao;
    }

    @Override
    public Either<RestaurantError, ArrayList<Table>> getAll() {
        return dao.getAll();
    }
}
