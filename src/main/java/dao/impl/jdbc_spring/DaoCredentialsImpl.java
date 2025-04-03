package dao.impl.jdbc_spring;

import common.constants.DbConstants;
import common.constants.ErrorConstants;
import dao.DaoCredentials;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Credential;
import model.error.RestaurantError;
import model.error.database.GeneralDatabaseError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoCredentialsImpl implements DaoCredentials {

    private ResultSet rs;
    private PreparedStatement pStmt;
    private final DBConnectionPool pool;

    @Inject
    public DaoCredentialsImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<RestaurantError, ArrayList<Credential>> getAll() {
        try (Connection con = pool.getConnection()) {
            pStmt = con.prepareStatement(QueryStrings.GET_ALL_CREDENTIALS);

            rs = pStmt.executeQuery();
            return Either.right(readRS(rs));

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, Credential> get(Credential credential) {
        try (Connection con = pool.getConnection()) {
            int id = credential.getId();
            pStmt = con.prepareStatement(QueryStrings.GET_CREDENTIAL_BY_ID);
            pStmt.setInt(1, id);

            rs = pStmt.executeQuery();
            List<Credential> credentials = readRS(rs);
            return Either.right(credentials.get(0));

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, Integer> save(Credential credential) {


        Either<RestaurantError, Integer> result;

        try (Connection con = pool.getConnection()) {
            result = saveCredential(credential, con);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            result = Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    private Either<RestaurantError, Integer> saveCredential(Credential credential, Connection con) {
        int affectedRows;
        Either<RestaurantError, Integer> result;
        try {
            pStmt = con.prepareStatement(QueryStrings.INSERT_CREDENTIAL);
            pStmt.setInt(1, credential.getId());
            pStmt.setString(2, credential.getUsername());
            pStmt.setString(3, credential.getPassword());

            affectedRows = pStmt.executeUpdate();

            result = Either.right(affectedRows);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            result = Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    private ArrayList<Credential> readRS(ResultSet rs) {
        ArrayList<Credential> listCredentials = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt(DbConstants.CUSTOMER_ID);
                String username = rs.getString(DbConstants.USERNAME);
                String password = rs.getString(DbConstants.PASSWORD);
                listCredentials.add(new Credential(id, username, password));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listCredentials;
    }

}
