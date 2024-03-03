package org.example;

import javax.swing.text.DateFormatter;
import java.lang.reflect.Executable;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class AttendanceUpdate {

    public void Updater(int employee_ID, LocalDate date, LocalTime time) throws SQLException {
        int flag = 0;
        System.out.println("Executing Updater");
        String[] attendanceDetails = attendanceIdFinder(employee_ID, date);
        LocalTime timeThreshold1 = LocalTime.parse("11:59:00");
        if(time.isBefore(timeThreshold1) || time.equals(timeThreshold1)){
            if(attendanceDetails[4] == null){
                MorningLogin(employee_ID, date, time);
            }else if(attendanceDetails[5] == null){
                MorningLogout(employee_ID, date, time);
            }else{
                /*Do Nothing*/
            }
        }else{
            if(!(attendanceDetails[4] == null) && (attendanceDetails[5] == null)){
                MorningLogout(employee_ID, date, time);
            } else if (attendanceDetails[6] == null) {
                AfternoonLogin(employee_ID, date, time);
            }else if (attendanceDetails[7] == null){
                AfternoonLogout(employee_ID, date, time);
            }else {
                /* Do Nothing */
            }
        }
        System.out.println("Finishing updater");
    }

    public void MorningLogin(int employee_ID, LocalDate date, LocalTime time) {
        System.out.println("Starts executing Morning Login");
        String month = date.getMonth().toString();
        String year = date.getYear() + "";
        RandomIDGenerator randomIDGenerator = new RandomIDGenerator();
        String attendanceID = randomIDGenerator.RandomIDGenerator();
        System.out.println("Month: " + month);
        System.out.println("Year: " + year);
        System.out.println("AttendanceId: " + attendanceID);
        String insertSQL = "INSERT INTO attendance (employee_id, attendance_id, date, time_in_morning, month, year) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
            System.out.println("preparing insertStatement");
            insertStatement.setInt(1, employee_ID);
            insertStatement.setString(2, attendanceID);
            insertStatement.setString(3, date.toString());
            insertStatement.setTime(4, Time.valueOf(time));
            insertStatement.setString(5, month);
            insertStatement.setString(6, year);
            System.out.println("insertStatement has been prepared");
            insertStatement.executeUpdate();
            System.out.println("Attendance Database Inserted with morning Login");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occured");
        }
        System.out.println("Finishing Morning Login");
    }

    public void MorningLogout(int employee_ID, LocalDate date, LocalTime time) throws SQLException {

        System.out.println("Executing Morning Logout");
        String attendanceID = "";
        String loginTime = "";
        String scheduleInTime = "";
        String scheduleOutTime = "";
        Double numHours;
        int statusMorning = 0;

        String resultAttendanceArray[] = attendanceIdFinder(employee_ID, date);
        System.out.println("Checking ResultAttendanceSet");
        attendanceID = resultAttendanceArray[2];
        loginTime = resultAttendanceArray[4];
        System.out.println("Attendance ID: " + attendanceID);
        System.out.println("login Time: " + loginTime);
        System.out.println("ResultAttendanceSet Tested");


        String[] resultScheduleArray = scheduleChecker(employee_ID);

        System.out.println("Checking ResultScheduleSet");
        scheduleInTime = resultScheduleArray[2];
        scheduleOutTime = resultScheduleArray[3];
        System.out.println("schedule Time In : " + scheduleInTime);
        System.out.println("schedule Time Out : " + scheduleOutTime);
        LocalTime login = LocalTime.parse(loginTime);
        LocalTime inTime = LocalTime.parse(scheduleInTime);
        LocalTime outTime = LocalTime.parse(scheduleOutTime);

        if(time.isBefore(login.minusMinutes(15))){
            System.exit(0);
        }

        statusMorning = ((login.isBefore(inTime) || (login.equals(inTime))) && (outTime.equals(time) || outTime.isAfter(time)))? 0: 1;
        Duration duration = Duration.between(login,time);
        numHours = (double)duration.toMinutes()/60;

        String updateSql = "UPDATE attendance SET time_out_morning = ?, status_morning = ?, num_hr_morning = ? WHERE attendance_id = ?";
        try(Connection connection = DBConnector.getConnection();
        PreparedStatement updateStatement = connection.prepareStatement(updateSql)){
            updateStatement.setString(1,time.toString());
            updateStatement.setInt(2,statusMorning);
            updateStatement.setDouble(3,numHours);
            updateStatement.setString(4,attendanceID);
            updateStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("finishing morning logout");
    }

    public void AfternoonLogin(int employee_ID, LocalDate date, LocalTime time) throws SQLException {

        System.out.println("Executing Afternoon login");
        String attendanceID = "";
        String loginTime = "";
        String sqlQuery = "";

        String[] resultAttendanceArray = attendanceIdFinder(employee_ID, date);
        System.out.println("Checking ResultAttendanceSet");
        attendanceID = resultAttendanceArray[2];
        loginTime = resultAttendanceArray[4];
        if(loginTime == null){
            RandomIDGenerator randomIDGenerator = new RandomIDGenerator();
            attendanceID = randomIDGenerator.RandomIDGenerator();
            sqlQuery = "INSERT INTO attendance(employee_id, attendance_id, date, time_in_afternoon, month, year) VALUES (?, ?, ?, ?, ?, ?)";
            try(Connection connection = DBConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
                preparedStatement.setInt(1,employee_ID);
                preparedStatement.setString(2, attendanceID);
                preparedStatement.setString(3, date.toString());
                preparedStatement.setString(4, time.toString());
                preparedStatement.setString(5,date.getMonth().toString());
                preparedStatement.setString(6,date.getYear() + "");
                System.out.println("Executing Query");
                System.out.println(preparedStatement.executeUpdate());
                System.out.println("Query Executed");
            }catch(SQLException e){
                e.printStackTrace();
            }
        }else{
            attendanceID = resultAttendanceArray[2];
            sqlQuery = "UPDATE attendance SET time_in_afternoon = ? WHERE attendance_id = ?";
            try(Connection connection = DBConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
                preparedStatement.setString(1,time.toString());
                preparedStatement.setString(2,attendanceID);
                System.out.println("Executing Query");
                System.out.println(preparedStatement.executeUpdate());
                System.out.println("Query Executed");
            }
        }

        System.out.println("Finishing Afternoon Login");
    }

    public void AfternoonLogout(int employee_ID, LocalDate date, LocalTime time) throws SQLException {
        System.out.println("Executing Afternoon Logout");
        String attendanceID = "";
        String loginTime = "";
        String scheduleInTime = "";
        String scheduleOutTime = "";
        Double numHours;
        int statusMorning = 0;

        String resultAttendanceArray[] = attendanceIdFinder(employee_ID, date);
        System.out.println("Checking ResultAttendanceSet");
        attendanceID = resultAttendanceArray[2];
        loginTime = resultAttendanceArray[6];
        System.out.println("Attendance ID: " + attendanceID);
        System.out.println("login Time: " + loginTime);
        System.out.println("ResultAttendanceSet Tested");
        String[] resultScheduleArray = scheduleChecker(employee_ID);

        System.out.println("Checking ResultScheduleSet");
        scheduleInTime = resultScheduleArray[2];
        scheduleOutTime = resultScheduleArray[3];
        System.out.println("schedule Time In : " + scheduleInTime);
        System.out.println("schedule Time Out : " + scheduleOutTime);
        LocalTime login = LocalTime.parse(loginTime);
        LocalTime inTime = LocalTime.parse(scheduleInTime);
        LocalTime outTime = LocalTime.parse(scheduleOutTime);

        if(time.isBefore(login.minusMinutes(15))){
            System.exit(0);
        }

        statusMorning = ((login.isBefore(inTime) || (login.equals(inTime))) && (outTime.equals(time) || outTime.isAfter(time)))? 0: 1;
        Duration duration = Duration.between(login,time);
        numHours = (double)duration.toMinutes()/60;

        String updateSql = "UPDATE attendance SET time_out_afternoon = ?, status_afternoon = ?, num_hr_afternoon = ? WHERE attendance_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(updateSql)){
            updateStatement.setString(1,time.toString());
            updateStatement.setInt(2,statusMorning);
            updateStatement.setDouble(3,numHours);
            updateStatement.setString(4,attendanceID);
            updateStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Finishing Afternoon Logout");
    }

    public static  String[] scheduleChecker(int emp_id){

        System.out.println("Executing schedule checker");
        String[] resultArray = new String[6];
        String selectSql = "SELECT schedule_id FROM employees WHERE id=?";
        String schedule_id = "";
        try(Connection connection = DBConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql)){
            preparedStatement.setInt(1, emp_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                schedule_id = resultSet.getString(1);
                System.out.println("Schedule ID: " +schedule_id);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        selectSql = "SELECT * FROM schedules WHERE id=?";
        try(Connection connection = DBConnector.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(selectSql)){
            selectStatement.setString(1, schedule_id);
            ResultSet newResultSet;
            newResultSet = selectStatement.executeQuery();
            if(newResultSet.next()){
                resultArray[0] = newResultSet.getString(1);
                resultArray[1] = newResultSet.getString(2);
                resultArray[2] = newResultSet.getString(3);
                resultArray[3] = newResultSet.getString(4);
                resultArray[4] = newResultSet.getString(5);
                resultArray[5] = newResultSet.getString(6);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println("Finishing Schedule Checker");
        return resultArray;
    }

    public static String[] attendanceIdFinder(int employeeID, LocalDate date){

        System.out.println("Executing Attendance-ID finder");
        String[] resultArray = new String[14];
        String attendanceId = "";
        String selectSql = "SELECT * FROM attendance WHERE employee_id = ? AND date = ?";
        try(Connection connection  = DBConnector.getConnection();
            PreparedStatement selectStatement = connection.prepareStatement(selectSql)){
                    selectStatement.setInt(1,employeeID);
                    selectStatement.setString(2, date.toString());

                    ResultSet resultSet = selectStatement.executeQuery();

                    if(resultSet.next()){
                        resultArray[0] = resultSet.getString(1); //attendance_id
                        resultArray[1] = resultSet.getString(2);
                        resultArray[2] = resultSet.getString(3);
                        resultArray[3] = resultSet.getString(4);
                        resultArray[4] = resultSet.getString(5);
                        resultArray[5] = resultSet.getString(6);
                        resultArray[6] = resultSet.getString(7);
                        resultArray[7] = resultSet.getString(8);
                        resultArray[8] = resultSet.getString(9);
                        resultArray[9] = resultSet.getString(10);
                        resultArray[10] = resultSet.getString(11);
                        resultArray[11] = resultSet.getString(12);
                        resultArray[12] = resultSet.getString(13);
                        resultArray[13] = resultSet.getString(14);//time_in_morning
                        System.out.println("Attendance Id : "  + resultArray[0]);
                        System.out.println("Time In Morning : "  + resultArray[1]);
                    }

        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Finishing Attendance-ID finder");
        return resultArray;
    }
}
