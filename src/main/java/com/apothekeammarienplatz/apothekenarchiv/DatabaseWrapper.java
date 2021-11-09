/*
 * Copyright (C) 2021 Martin Mandelkow <mandelkow@apotheke-schwerin.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.apothekeammarienplatz.apothekenarchiv;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Mandelkow <mandelkow@apotheke-schwerin.de>
 */
public class DatabaseWrapper {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public DatabaseWrapper() {
        try {
            /**
             * Read database parameters:
             */
            ReadPropertyFile readPropertyFile = new ReadPropertyFile();
            String databasePassword = readPropertyFile.getDatabasePassword();
            String databaseUser = readPropertyFile.getDatabaseUser();
            String databaseHost = readPropertyFile.getDatabaseHost();
            String databaseName = readPropertyFile.getDatabaseName();
            // This will load the MySQL driver, each DB has its own driver
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + databaseHost + "/" + databaseName + "?" + "user=" + databaseUser + "&password=" + databasePassword);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param sqlQuery
     * @return prepared statement
     */
    public PreparedStatement prepareStatement(String sqlQuery) {
        try {
            preparedStatement = connect.prepareStatement(sqlQuery);
            return preparedStatement;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //close();
        }
        return preparedStatement;
    }

    public ResultSet runQuery(String sqlQuery, List<String> argumentArray) {
        try {
            preparedStatement = prepareStatement(sqlQuery);
            for (int i = 1; i < argumentArray.size(); i++) {
                preparedStatement.setString(i, argumentArray.get(i));
            }
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return resultSet;
    }

    public void readDataBase() throws Exception {
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        String databasePassword = readPropertyFile.getDatabasePassword();
        String databaseUser = readPropertyFile.getDatabaseUser();
        String databaseHost = readPropertyFile.getDatabaseHost();
        String databaseName = readPropertyFile.getDatabaseName();
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + databaseHost + "/" + databaseName + "?" + "user=" + databaseUser + "&password=" + databasePassword);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from stoffe");
            writeResultSet(resultSet);

            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into stoffe values (?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatement.setInt(1, 1234);
            preparedStatement.setString(2, "Test");
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getString(2);
            String stoffId = resultSet.getString("stoffId");
            String stoffName = resultSet.getString("stoffName");
            System.out.println("stoffId: " + stoffId);
            System.out.println("stoffName: " + stoffName);
        }
    }

    // You need to close the resultSet
    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}
