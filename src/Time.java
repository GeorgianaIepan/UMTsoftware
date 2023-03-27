import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class Time {  // clasa echivalenta cu timpul ocupat pentru o intalnire
    private Date start;  // inceputul intervalului unei intalniri
    private Date end;  // sfarsitul intervalului unei intalniri

    String pattern = "HH:mm";  // formatul tipului Date: "ore:minute"
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public Time(String start, String end) {  // constructor - se foloseste pentru formarea obiectului
        try {
            this.start = simpleDateFormat.parse(start);  // parse - transforma tipul String in Date, folosind pattern-ul dat
            this.end = simpleDateFormat.parse(end);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getStart() {
        return start;
    }  // getter

    public String getStringStart(){  // getter - returneaza Date ca String
        String stringStart = simpleDateFormat.format(start);  // format - transforma tipul Date in String, folosind pattern-ul dat
        return stringStart;
    }

    public void setStart(Date start) {
        this.start = start;
    }  // setter

    public Date getEnd() {
        return end;
    }  // getter

    public String getStringEnd(){  // getter - returneaza Date ca String
        String stringEnd = simpleDateFormat.format(end);
        return stringEnd;
    }

    public void setEnd(Date end) {
        this.end = end;
    }  // setter

    @Override
    public String toString() {  // metoda pentru afisarea unui obiect Time ca String
        String timeStart = simpleDateFormat.format(start);
        String timeEnd = simpleDateFormat.format(end);

        return "[" + timeStart + ", " + timeEnd + "]";
    }
}
