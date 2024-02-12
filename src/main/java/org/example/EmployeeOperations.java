package org.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class EmployeeOperations {
    public static void insertEmployee(int empId, String empName) {
        String sql = "INSERT INTO Employee (emp_id, emp_name) VALUES (?, ?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, empId);
            preparedStatement.setString(2, empName);

            preparedStatement.executeUpdate();

            System.out.println("Employee record inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
