package com.github.bachelorpraktikum.dbvisualization.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBTable {

    private Tables table;
    private String select_query_string = "SELECT %s FROM %s%s;";
    private PreparedStatement select_query_statement;
    private ResultSet select_result;

    public DBTable(Connection connection, Tables table) throws SQLException {
        this.table = table;
        select_query_string = String
            .format(select_query_string, getColumnNamesAsString(), getName(),
                table.getWhereCondition().orElse(""));
        createSelectStatement(connection);
    }

    DBTable() {
    }

    private void createSelectStatement(Connection connection) throws SQLException {
        select_query_statement = connection.prepareStatement(select_query_string);
    }

    /**
     * Executes a select query over all column names (`getColumnNamesAsString`)
     *
     * @return ResultSet for 'SELECT `getColumnNamesAsString` FROM `getName()`'
     */
    public ResultSet select() throws SQLException {
        return select(false);
    }

    ResultSet select(boolean update) throws SQLException {
        if (select_result != null && !update) {
            return select_result;
        }
        select_result = select_query_statement.executeQuery();
        return select_result;
    }

    List<String> getColumnNames() {
        return table.getColumnNames();
    }

    /**
     * Returns a comma seperated list of column names.
     * Column names are retrieved by `getColumnNames`.
     *
     * @return Column names (comma seperated)
     */
    String getColumnNamesAsString() {
        return getColumnNames().stream().reduce((s, s1) -> s + ", " + s1).get();
    }

    String getName() {
        return table.getName();
    }
}
