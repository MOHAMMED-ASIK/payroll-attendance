package org.example;
import java.sql.Timestamp;
public class Main {
    public static void main(String[] args) {

                //EmployeeOperations.insertEmployee(3, "manoj");
                AttendanceUpdater.updateAttendance(9, Timestamp.valueOf("2024-01-31 09:00:00"),
                        Timestamp.valueOf("2024-01-31 17:00:00"));
            }
        }


