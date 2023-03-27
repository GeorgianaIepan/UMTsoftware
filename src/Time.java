import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class Time {
    private Date start;
    private Date end;

    String pattern = "HH:mm";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public Time(String start, String end) {
        try {
            this.start = simpleDateFormat.parse(start);
            this.end = simpleDateFormat.parse(end);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public Date getStart() {
        return start;
    }

    public String getStringStart(){
        String stringStart = simpleDateFormat.format(start);
        return stringStart;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public String getStringEnd(){
        String stringEnd = simpleDateFormat.format(end);
        return stringEnd;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        String timeStart = simpleDateFormat.format(start);
        String timeEnd = simpleDateFormat.format(end);

        return "[" + timeStart + ", " + timeEnd + "]";
    }
}
