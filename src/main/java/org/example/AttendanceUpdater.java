package org.example;

import java.sql.*;
import java.time.LocalTime;

public class AttendanceUpdater {
    public static void updateAttendance(int empId, LocalTime Time) {
            boolean isLoginTime = false;
            String selectSql = "SELECT * FROM Attendance WHERE emp_id = ? AND login_time IS NOT NULL";
            try (Connection connection = DBConnector.getConnection();
                 PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, empId);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    isLoginTime = true; // Employee has already logged in
                }
            } catch (SQLException e) {
                System.err.println("Error occurred while checking existing login time:");
                e.printStackTrace();
                return;
            }

            // Insert the time into the appropriate column based on whether it's a login or logout time
            String insertSql;
            if (isLoginTime) {
                insertSql = "UPDATE Attendance SET logout_time = ? WHERE emp_id = ? AND logout_time IS NULL";
            } else {
                insertSql = "INSERT INTO Attendance (emp_id, login_time) VALUES (?, ?)";
            }

            try (Connection connection = DBConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

                if (isLoginTime) {
                    preparedStatement.setTime(1, java.sql.Time.valueOf(Time));
                    preparedStatement.setInt(2, empId);
                } else {
                    preparedStatement.setInt(1, empId);
                    preparedStatement.setTime(2, java.sql.Time.valueOf(Time));
                }

                preparedStatement.executeUpdate();

                System.out.println("Attendance record updated successfully.");
            } catch (SQLException e) {
                System.err.println("Error occurred while updating attendance record:");
                e.printStackTrace();
            }


        }
    }
