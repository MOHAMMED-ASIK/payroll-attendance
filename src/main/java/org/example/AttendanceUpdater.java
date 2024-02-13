package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttendanceUpdater {
    public static void updateAttendance(int empId, Timestamp loginTime, Timestamp logoutTime) {

            String sql = "INSERT INTO Attendance (emp_id, login_time, logout_time) VALUES (?, ?, ?)";
            try  {
                Connection connection = DBConnector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, empId);
                preparedStatement.setTimestamp(2, loginTime);
                preparedStatement.setTimestamp(3, logoutTime);

                preparedStatement.executeUpdate();

                System.out.println("Attendance record updated successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



