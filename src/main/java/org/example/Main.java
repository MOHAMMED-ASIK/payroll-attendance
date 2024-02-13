package org.example;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //EmployeeOperations.insertEmployee(3, "manoj");
        Scanner ob = new Scanner(System.in);
        System.out.println("Enter id");
        int id = ob.nextInt();
        System.out.println("Enter the time (HH:MM:SS):");
        String inputTime = ob.next();
        LocalTime parsedTime = LocalTime.parse(inputTime);
        AttendanceUpdater.updateAttendance(id,parsedTime);
       }
}


