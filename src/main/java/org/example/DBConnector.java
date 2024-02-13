package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DBConnector {
        private static final String JDBC_URL = "jdbc:mysql://localhost:3306/attendance";
        private static final String USER = "root";
        private static final String PASSWORD = "Asik2002$$$$";

        public static Connection getConnection() throws SQLException{
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        }
    }


