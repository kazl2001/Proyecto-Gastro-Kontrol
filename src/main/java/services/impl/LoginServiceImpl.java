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
        Either<RestaurantError, ArrayList<Credential>> credentialsResult = daoCredentials.getAll();

        if (credentialsResult.isLeft()) {
            return Either.left(credentialsResult.getLeft());
        }

        List<Credential> credentials = credentialsResult.get();

        if (user == null || pass == null) {
            return Either.left(new RestaurantError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        }

        Optional<Credential> matchingCredential = credentials.stream()
                .filter(credential -> credential.getUsername().equals(user) && credential.getPassword().equals(pass))
                .findFirst();

        if (matchingCredential.isPresent()) {
            int id = matchingCredential.get().getId();
            return Either.right(id);
        } else {
            return Either.left(new RestaurantError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        }
    }

    @Override
    public boolean checkAdmin(String user, String pass) {
        final String adminUser;
        final String adminPass;
        AtomicReference<String> userRef = new AtomicReference<>();
        AtomicReference<String> passRef = new AtomicReference<>();

        Credential c = new Credential(1); //el admin tiene el customer_id = 1

        daoCredentials.get(c).peek(credential -> {
            userRef.set(credential.getUsername());
            passRef.set(credential.getPassword());
        });

        adminUser = userRef.get();
        adminPass = passRef.get();

        return user.equals(adminUser) && pass.equals(adminPass);
    }
}
