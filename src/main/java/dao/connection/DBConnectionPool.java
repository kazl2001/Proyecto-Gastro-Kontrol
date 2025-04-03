package dao.connection;

import common.config.ConfigJDBC;
import common.constants.Constants;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
@Log4j2
public class DBConnectionPool {

    private final ConfigJDBC config;

    private final DataSource hikariDataSource;

    @Inject
    public DBConnectionPool(ConfigJDBC config) {
        this.config = config;
        hikariDataSource = getHikariPool();
    }

    public DataSource getHikariPool() {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(config.getProperty(Constants.JDBC_URL));
        hikariConfig.setUsername(config.getProperty(Constants.USERNAME));
        hikariConfig.setPassword(config.getProperty(Constants.PASSWORD));
        hikariConfig.setDriverClassName(config.getProperty(Constants.DRIVER));
        hikariConfig.setMaximumPoolSize(4);

        hikariConfig.addDataSourceProperty(Constants.CACHE_PREP_STATEMENTS, true);
        hikariConfig.addDataSourceProperty(Constants.PREP_STMT_CACHE_SIZE, 250);
        hikariConfig.addDataSourceProperty(Constants.PREP_STMT_CACHE_SQL_LIMIT, 2048);

        return new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() {
        Connection c = null;
        try {
            c = hikariDataSource.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return c;
    }

    public DataSource getDataSource() {
        return hikariDataSource;
    }

    public void closeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void closePool() {
        ((HikariDataSource) hikariDataSource).close();
    }


}


