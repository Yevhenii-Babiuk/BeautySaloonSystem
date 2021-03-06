package com.saloon.beauty.repository;

import java.sql.*;

/**
 * Contains util methods for DAOs
 */
public class DBUtils {

    /**
     * Returns id from executed insert statement
     * @param insertStatement - executed statement
     * @return - autogenerated by DB id or -1 if {@code Statement}  object did
     * not generate any keys
     * @throws SQLException if a database access error occurs or
     * this method is called on a closed {@code Statement}
     */
    public static long getIdFromStatement(Statement insertStatement) throws SQLException {

        long id = -1;

        ResultSet generatedKeys = insertStatement.getGeneratedKeys();

        if (generatedKeys.next()) {
            id = generatedKeys.getLong(1);
        }

        return id;
    }


    /**
     * Checks is a given exception thrown because of attempt to insert
     * into a table a duplicate entry. In many popular SQL servers such as
     * MySQL the exception it this case will return
     * SQLState = "23000"
     * @param e - exception thrown by INSERT statement execute
     * @return true if the INSERT statement query met the duplicate in the table
     */
    public static boolean isTryingToInsertDuplicate(SQLException e) {
        return "1062".equals(e.getSQLState());
    }

}
