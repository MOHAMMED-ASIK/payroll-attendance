package org.attendance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DBConnector {
        private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection getConnection() throws SQLException{
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        }
    }


