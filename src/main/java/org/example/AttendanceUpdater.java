package org.example;

import java.sql.*;
import java.time.LocalTime;

public class AttendanceUpdater {
    public static void updateAttendance(int empId, LocalTime Time) {

            String sql = "INSERT INTO Attendance (emp_id, login_time,logout_time) VALUES (?,?,?)";
            try  {
                Connection connection = DBConnector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, empId);

                preparedStatement.setTime(2, java.sql.Time.valueOf(Time));
               preparedStatement.setTime(3,java.sql.Time.valueOf(Time));

                preparedStatement.executeUpdate();

                System.out.println("Attendance record updated successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



