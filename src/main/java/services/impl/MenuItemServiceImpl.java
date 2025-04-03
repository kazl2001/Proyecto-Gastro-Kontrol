package services.impl;

import dao.DaoMenuItems;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.error.RestaurantError;
import services.MenuItemService;

import java.util.ArrayList;

public class MenuItemServiceImpl implements MenuItemService {

    private final DaoMenuItems dao;

    @Inject
    public MenuItemServiceImpl(DaoMenuItems dao) {
        this.dao = dao;
    }

    @Override
    public Either<RestaurantError, ArrayList<MenuItem>> getAll() {
        return dao.getAll();
    }

}
