package services.impl;

import common.constants.Constants;
import common.constants.ErrorConstants;
import dao.DaoCredentials;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credential;
import model.error.RestaurantError;
import model.error.customer.CustomerValidationError;
import services.CredentialService;

import java.util.ArrayList;

public class CredentialServiceImpl implements CredentialService {

    private final DaoCredentials daoJDBC;

    @Inject
    public CredentialServiceImpl(DaoCredentials daoJDBC) {
        this.daoJDBC = daoJDBC;
    }

    @Override
    public Either<RestaurantError, ArrayList<Credential>> getAll(){
        return daoJDBC.getAll();
    }

    @Override
    public Either<RestaurantError, Credential> get(int id){
        Credential credential = new Credential(id);
        return daoJDBC.get(credential);
    }

    @Override
    public Either<RestaurantError, Integer> save(Credential credential) {
        if (credential != null) {
            return daoJDBC.save(credential);
        } else {
            return Either.left(new CustomerValidationError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        }
    }
}
