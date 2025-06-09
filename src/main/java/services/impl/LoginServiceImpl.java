package services.impl;

import common.constants.Constants;
import common.constants.ErrorConstants;
import dao.DaoCredentials;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credential;
import model.error.RestaurantError;
import services.LoginService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class LoginServiceImpl implements LoginService {

    private final DaoCredentials daoCredentials;

    @Inject
    public LoginServiceImpl(DaoCredentials daoCredentials) {
        this.daoCredentials = daoCredentials;
    }

    @Override
    public Either<RestaurantError, Integer> login(String user, String pass) {
        if (user == null || pass == null || user.isBlank() || pass.isBlank()) {
            return Either.left(new RestaurantError(
                    ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE,
                    ErrorConstants.INVALID_ENTITY_DATA_ERROR
            ));
        }

        Either<RestaurantError, Credential> authResult = daoCredentials.authenticate(user, pass);

        return authResult.map(Credential::getId);
    }


    @Override
    public boolean checkAdmin(String user, String pass) {
        // ADMIN = id 1
        Either<RestaurantError, Credential> authResult = daoCredentials.authenticate(user, pass);
        return authResult.isRight() && authResult.get().getId() == 1;
    }

}
