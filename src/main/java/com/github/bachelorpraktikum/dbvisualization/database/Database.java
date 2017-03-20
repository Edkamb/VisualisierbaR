package com.github.bachelorpraktikum.dbvisualization.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Database implements AutoCloseable {

    private HikariDataSource dataSource;
    private final int CONNECTION_TIMEOUT = 1000;

    public Database(URI uri) {
        new Database(uri, new DatabaseUser("", ""));
    }

    public Database(URI uri, DatabaseUser user) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(uri.toString().replace("http://", "jdbc:mysql://"));
        config.setUsername(user.getUser());
        config.setPassword(user.getPassword());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    public boolean testConnection() {
        try {
            return dataSource.getConnection().isValid(CONNECTION_TIMEOUT);
        } catch (SQLException ignored) {
            return false;
        }
    }

    public Optional<Connection> getConnection() {
        try {
            return Optional.of(dataSource.getConnection());
        } catch (SQLException ignored) {

        }

        return Optional.empty();
    }

    public List<Vertex> getVertices() {
        List<Vertex> vertices = new LinkedList<>();
        try {
            DBTable verticesTable = new DBTable(getConnection().get(), Tables.VERTICES);
            ResultSet rs = verticesTable.select();
            while (rs.next()) {
                vertices.add(new Vertex(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vertices;
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}
