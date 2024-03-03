package simulator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Data implements Serializable {
    private int employee_ID;
    private LocalDate date;
    private LocalTime time;

    public Data(int employee_ID, LocalTime time, LocalDate date){

        this.employee_ID = employee_ID;
        this.date = date;
        this.time = time;
    }

    public int getEmployee_ID(){
        return employee_ID;
    }

    public void setEmployee_ID(int employee_ID){
        this.employee_ID = employee_ID;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public LocalTime getTime(){
        return time;
    }

    public void setTime(LocalTime time){
        this.time = time;
    }

}
