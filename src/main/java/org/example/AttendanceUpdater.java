package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttendanceUpdater {
    public static void updateAttendance(int empId, Timestamp loginTime, Timestamp logoutTime) {
        String employeeName = getEmployeeName(empId);

        if (employeeName != null) {
            String sql = "INSERT INTO Attendance (emp_id, emp_name, login_time, logout_time) VALUES (?, ?, ?, ?)";

            try (Connection connection = DBConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, empId);
                preparedStatement.setString(2, employeeName);
                preparedStatement.setTimestamp(3, loginTime);
                preparedStatement.setTimestamp(4, logoutTime);

                preparedStatement.executeUpdate();

                System.out.println("Attendance record updated successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Employee not found with ID: " + empId);
        }
    }

    private static String getEmployeeName(int empId) {
        String sql = "SELECT emp_name FROM Employee WHERE emp_id = ?";
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, empId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("emp_name");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

