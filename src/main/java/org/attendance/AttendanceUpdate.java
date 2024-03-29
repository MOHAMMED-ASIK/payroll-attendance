package org.attendance;

import javax.swing.text.DateFormatter;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public Boolean MorningLogin(int employee_ID, LocalDate date, LocalTime time) {
        System.out.println("--------------------Starts executing Morning Login-------------------");
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
            System.out.println("Preparing insertStatement..........");
            insertStatement.setInt(1, employee_ID);
            insertStatement.setString(2, attendanceID);
            insertStatement.setString(3, date.toString());
            insertStatement.setTime(4, Time.valueOf(time));
            insertStatement.setString(5, month);
            insertStatement.setString(6, year);
            System.out.println("insertStatement has been prepared 👍");
            insertStatement.executeUpdate();
            System.out.println("Attendance Database Inserted with morning Login");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to insert morning Login");
            return false;
        }
        System.out.println("----------Finishing Morning Login-----------");
        return true;
    }

    public Boolean MorningLogout(int employee_ID, LocalDate date, LocalTime time) throws SQLException {

        System.out.println("----------Executing Morning Logout--------------");
        String attendanceID = "";
        String loginTime = "";
        String scheduleInTime = "";
        String scheduleOutTime = "";
        Double numHours;
        int statusMorning = 0;

        String resultAttendanceArray[] = attendanceIdFinder(employee_ID, date);
        System.out.println("---------Checking ResultAttendanceSet----------");
        attendanceID = resultAttendanceArray[2];
        loginTime = resultAttendanceArray[4];
        System.out.println("Attendance ID: " + attendanceID);
        System.out.println("login Time: " + loginTime);
        System.out.println("------------ResultAttendanceSet Tested-------------");


        String[] resultScheduleArray = scheduleChecker(employee_ID);

        System.out.println("------------Checking ResultScheduleSet--------------");
        scheduleInTime = resultScheduleArray[2];
        scheduleOutTime = resultScheduleArray[3];

        LocalTime login = LocalTime.parse(loginTime);
        LocalTime inTime = LocalTime.parse(scheduleInTime);
        LocalTime outTime = LocalTime.parse(scheduleOutTime);
        statusMorning = ((login.isBefore(inTime) || (login.equals(inTime))) && (time.equals(outTime) || time.isAfter(outTime)))? 1: 0;
        Duration duration = Duration.between(login,time);
        numHours = (double)duration.toMinutes()/60;
        System.out.println("Data successfully parsed");
        if(numHours <= 0.25){
            System.out.println("Recently Logged in. Try Again After sometime ⚠️");
            System.out.println("--------------finishing morning logout-----------------");

            return false;
        }

        String updateSql = "UPDATE attendance SET time_out_morning = ?, status_morning = ?, num_hr_morning = ? WHERE attendance_id = ?";
        try(Connection connection = DBConnector.getConnection();
        PreparedStatement updateStatement = connection.prepareStatement(updateSql)){
            updateStatement.setString(1,time.toString());
            updateStatement.setInt(2,statusMorning);
            updateStatement.setDouble(3,numHours);
            updateStatement.setString(4,attendanceID);
            updateStatement.executeUpdate();
            System.out.println("Morning Logout successfully updated");
            System.out.println("--------------finishing morning logout-----------------");
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Failed to execute Morning Logout");
            System.out.println("--------------finishing morning logout-----------------");
            return false;
        }
    }

    public Boolean AfternoonLogin(int employee_ID, LocalDate date, LocalTime time) throws SQLException {

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
                System.out.println("Query Executed...✅");
                System.out.println("No Morning Login Found. Afternoon Login Inserted... ✅");
                System.out.println("---------Finishing Afternoon Login------------");

                return true;
            }catch(SQLException e){
                e.printStackTrace();
                System.out.println("No morning Login Found. Problem with inserting Afternoon Login... ❌");
                System.out.println("---------Finishing Afternoon Login------------");

                return false;
            }
        }else{
            attendanceID = resultAttendanceArray[2];
            sqlQuery = "UPDATE attendance SET time_in_afternoon = ? WHERE attendance_id = ?";
            try(Connection connection = DBConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
                preparedStatement.setString(1,time.toString());
                preparedStatement.setString(2,attendanceID);
                System.out.println("Executing Query...");
                System.out.println(preparedStatement.executeUpdate());
                System.out.println("Query Executed...✅");
                System.out.println("Morning Login Found. Afternoon Login Updated...✅");
                System.out.println("---------Finishing Afternoon Login------------");
                return  true;

            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Morning Login Found. Unable to update Afternoon Login...❌");
                System.out.println("---------Finishing Afternoon Login------------");
                return false;
            }
        }
    }

    public Boolean AfternoonLogout(int employee_ID, LocalDate date, LocalTime time) throws SQLException {
        System.out.println("---------------Executing Afternoon Logout---------------");
        String attendanceID = "";
        String loginTime = "";
        String scheduleInTime = "";
        String scheduleOutTime = "";
        Double numHours;
        int statusAfternoon = 0;

        String resultAttendanceArray[] = attendanceIdFinder(employee_ID, date);
        System.out.println("Checking ResultAttendanceSet.....");
        attendanceID = resultAttendanceArray[2];
        loginTime = resultAttendanceArray[6];
        String[] resultScheduleArray = scheduleChecker(employee_ID);
        System.out.println("Status okay ✅");

        System.out.println("Checking ResultScheduleSet.......");
        scheduleInTime = resultScheduleArray[4] + " PM";
        scheduleOutTime = resultScheduleArray[5] + " PM";
        System.out.println("Status okay ✅");
        LocalTime login = LocalTime.parse(loginTime);//11:00
        scheduleInTime = LocalTime.parse(scheduleInTime, DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.US))
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        scheduleOutTime = LocalTime.parse(scheduleOutTime, DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.US))
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime inTime =LocalTime.parse(scheduleInTime);
        LocalTime outTime = LocalTime.parse(scheduleOutTime);//13:00   //15:00
        System.out.println(inTime);
        System.out.println(outTime);
        System.out.println("Data successfully parsed ✅");
        System.out.println((login.isBefore(inTime) || (login.equals(inTime))));
        System.out.println((time.equals(outTime) || time.isAfter(outTime)));
        statusAfternoon = ((login.isBefore(inTime) || (login.equals(inTime))) && (time.equals(outTime) || time.isAfter(outTime)))? 1: 0;
        Duration duration = Duration.between(login,time);
        numHours = (double)duration.toMinutes()/60;
        if(numHours <= 0.25){
            System.out.println("Recently Logged in. Try Again After sometime ⚠️");
            System.out.println("--------------finishing afternoon logout-----------------");

            return false;
        }

        String updateSql = "UPDATE attendance SET time_out_afternoon = ?, status_afternoon = ?, num_hr_afternoon = ? WHERE attendance_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(updateSql)){
            updateStatement.setString(1,time.toString());
            updateStatement.setInt(2,statusAfternoon);
            updateStatement.setDouble(3,numHours);
            updateStatement.setString(4,attendanceID);
            updateStatement.executeUpdate();
            System.out.println("After logout executed");
            System.out.println("----------------------Finishing Afternoon Logout---------------------");
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("afternoon logout failed");
            System.out.println("----------------------Finishing Afternoon Logout---------------------");
            return  false;
        }
    }

    public static  String[] scheduleChecker(int emp_id){

        System.out.println("----------------------Executing schedule checker---------------------");
        String[] resultArray = new String[6];
        String selectSql = "SELECT schedule_id FROM employees WHERE id=?";
        String schedule_id = "";
        try(Connection connection = DBConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql)){
            preparedStatement.setInt(1, emp_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                schedule_id = resultSet.getString(1);
                System.out.println("getting schedule id successful..✅.");
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("unable to get schedule id....❌");
        }

        selectSql = "SELECT * FROM schedules WHERE id=?";
        try(Connection connection = DBConnector.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(selectSql)){
            selectStatement.setString(1, schedule_id);
            ResultSet newResultSet;
            newResultSet = selectStatement.executeQuery();
            if(newResultSet.next()){
                for (int i = 0; i<resultArray.length; i++){
                    resultArray[i] = newResultSet.getString(i+1);
                }
            }
            System.out.println("getting schedule details successful ✅");

        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("unable to get schedule details ❌");
        }
        System.out.println("-----------Finishing Schedule Checker-----------");
        return resultArray;
    }

    public static String[] attendanceIdFinder(int employeeID, LocalDate date){

        System.out.println("--------Executing Attendance-ID finder----------");
        String[] resultArray = new String[14];
        String attendanceId = "";
        String selectSql = "SELECT * FROM attendance WHERE employee_id = ? AND date = ?";
        try(Connection connection  = DBConnector.getConnection();
            PreparedStatement selectStatement = connection.prepareStatement(selectSql)){
                    selectStatement.setInt(1,employeeID);
                    selectStatement.setString(2, date.toString());

                    ResultSet resultSet = selectStatement.executeQuery();

                    if(resultSet.next()){
                        for(int i = 0; i< resultArray.length; i++){
                            resultArray[i] = resultSet.getString(i+1);
                        }
                    }
                System.out.println("getting attendance details successful...✅");
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Unable to get attendance details...❌");

        }

        System.out.println("---------------Finishing Attendance-ID finder-------------");
        return resultArray;
    }
}
