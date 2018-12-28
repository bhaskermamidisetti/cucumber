package com.org.ui.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

class MysqlConnection {

    private static final Logger logger = LoggerFactory.getLogger(MysqlConnection.class);

    public static Connection getDBConnection() {

        Connection connection;

        String mySqlDriver = "com.mysql.cj.jdbc.Driver";
        String urlString = "jdbc:mysql://localhost:3306/mysql";
        String userName = "root";
        String password = "root";

        /*String mySqlDriver = readXMLdata.getTestData("DB/MySql", "MySqlDriver");
        String urlString = readXMLdata.getTestData("DB/MySql", "DBConnectionStringLocal");
        String userName = readXMLdata.getTestData("DB/MySql", "UserName");
        String password = readXMLdata.getTestData("DB/MySql", "Password");*/

        try {
            Class.forName(mySqlDriver);
            connection = DriverManager.getConnection(urlString, userName, password);
            System.out.println("Connection success");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return connection;
    }


    public static void getResultSet() {

        String querry = "select * from db";
        try (Connection connection = getDBConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(querry)) {
                while (rs.next()) {
                    System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {

        getResultSet();

    }





}