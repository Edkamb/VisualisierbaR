package com.github.bachelorpraktikum.dbvisualization.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    public List<Neighbors> getNeighbors() {
        return getTableElements(Tables.NEIGHBORS, Neighbors.class);
    }

    public List<Attribute> getAttributes() {
        return getTableElements(Tables.ATTRIBUTES, Attribute.class);
    }

    public List<Betriebsstelle> getBetriebsstellen() {
        return getTableElements(Tables.BETRIEBSSTELLEN, Betriebsstelle.class);
    }

    public List<ObjectAttribute> getObjectAttributes() {
        return getTableElements(Tables.OBJECT_ATTRIBUTES, ObjectAttribute.class);
    }

    public List<ObjectObjectAttribute> getObjectObjectAtributes() {
        return getTableElements(Tables.OBJECT_OBJECT_ATTRIBUTES, ObjectObjectAttribute.class);
    }

    public List<Vertex> getVertices() {
        return getTableElements(Tables.VERTICES, Vertex.class);
    }

    public List<DBEdge> getEdges() {
        return getTableElements(Tables.EDGES, DBEdge.class);
    }

    private <T> List<T> getTableElements(Tables table, Class<T> clazz) {
        List<T> elements = new LinkedList<>();
        try {
            DBTable tTable = new DBTable(getConnection().get(), table);
            ResultSet rs = tTable.select();
            while (rs.next()) {
                Constructor<T> construct = clazz.getConstructor(ResultSet.class);
                T element = construct.newInstance(rs);
                elements.add(element);
            }
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return elements;
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}
