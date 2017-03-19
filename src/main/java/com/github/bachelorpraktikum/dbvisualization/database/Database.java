package com.github.bachelorpraktikum.dbvisualization.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;
import java.sql.SQLException;

public class Database implements AutoCloseable {

    private HikariDataSource dataSource;
    private final int CONNECTION_TIMEOUT = 1000;

    public Database(URI uri) {
        new Database(uri, new DatabaseUser("", ""));
    }

    public Database(URI uri, DatabaseUser user) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(uri.toString().replace("http://", "jdbc:mysql://"));
        config.setUsername(user.getUser());
        config.setPassword(user.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    public boolean testConnection() throws SQLException {
        return dataSource.getConnection().isValid(CONNECTION_TIMEOUT);
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}
