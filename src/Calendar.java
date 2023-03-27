import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Calendar {
    private ArrayList<Time> bookedTime;
    private Time limits;

    public Calendar(ArrayList<Time> bookedTime, Time limits) {
        this.bookedTime = bookedTime;
        this.limits = limits;
    }

    public ArrayList<Time> getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(ArrayList<Time> bookedTime) {
        this.bookedTime = bookedTime;
    }

    public Time getLimits() {
        return limits;
    }

    public void setLimits(Time limits) {
        this.limits = limits;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "bookedTime: " + Arrays.toString(new ArrayList[]{bookedTime}) +
                ", limits: " + limits +
                '}';
    }
}
