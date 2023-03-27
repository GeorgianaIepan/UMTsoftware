import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Calendar {  // clasa
    private ArrayList<Time> bookedTime;  // lista de intalniri stabilite / timpul ocupat
    private Time limits;  // intervalul in care se pot stabilii intalniri pentru un calendar

    public Calendar(ArrayList<Time> bookedTime, Time limits) {  // constructor
        this.bookedTime = bookedTime;
        this.limits = limits;
    }

    public ArrayList<Time> getBookedTime() {
        return bookedTime;
    } // getter

    public void setBookedTime(ArrayList<Time> bookedTime) {
        this.bookedTime = bookedTime;
    }  // setter

    public Time getLimits() {
        return limits;
    }  // getter

    public void setLimits(Time limits) {
        this.limits = limits;
    }  //setter

    @Override
    public String toString() {  // metoda pentru afisarea unui obiect Calendar ca String
        return "Calendar{" +
                "bookedTime: " + Arrays.toString(new ArrayList[]{bookedTime}) +
                ", limits: " + limits +
                '}';
    }
}
