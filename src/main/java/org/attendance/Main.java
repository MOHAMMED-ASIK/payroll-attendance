package org.attendance;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.simulator.Data;


public class Main {
    public static void main(String[] args) {
        //EmployeeOperations.insertEmployee(3, "manoj");
//        Scanner ob = new Scanner(System.in);
//        System.out.println("Enter id");
//        int id = ob.nextInt();
//        System.out.println("Enter the time (HH:MM:SS):");
//        String inputTime = ob.next();
//        LocalTime parsedTime = LocalTime.parse(inputTime);
//        AttendanceUpdate.updateAttendance(id, parsedTime);
        AttendanceUpdate updater = new AttendanceUpdate();

        while(true){
            try{
                ServerSocket serverSocket = new ServerSocket(3000);
                System.out.println("Server started. Waiting for client...");

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected");

                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                Data recievedData = (Data) in.readObject();

                int employee_ID = recievedData.getEmployee_ID();
                LocalDate Date = recievedData.getDate();
                LocalTime Time = LocalTime.parse("15:00:00");

                updater.Updater(employee_ID, Date, Time);
                serverSocket.close();
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

       }
}


