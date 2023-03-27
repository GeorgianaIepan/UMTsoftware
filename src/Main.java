import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static final long ONE_MINUTE_IN_MILLIS = 60000;
    static String pattern = "HH:mm";
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public static Date addMinutesToDate(int minutes, Date beforeTime) {
        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static ArrayList<Time> meetings(Calendar calendar1, Calendar calendar2, Date meetingTimeMin) {
        ArrayList<Time> availableTime = new ArrayList<>();

        String startLim = null;
        String endLim = null;

        if (calendar1.getLimits().getStart().compareTo(calendar2.getLimits().getStart()) < 0) {  // calendar1 start lim < calendar2 start lim
            startLim = calendar2.getLimits().getStringStart();
        } else {
            startLim = calendar1.getLimits().getStringStart();
        }

        if (calendar1.getLimits().getEnd().compareTo(calendar2.getLimits().getEnd()) < 0) {  // calendar1 end lim < calendar2 end lim
            endLim = calendar1.getLimits().getStringEnd();
        } else {
            endLim = calendar2.getLimits().getStringEnd();
        }

        Time availableLim = new Time(startLim, endLim);
        //Calendar availableCalendar = new Calendar(availableTime, availableLim);
        System.out.println("Avv lim:" + availableLim);

        LinkedHashMap<Date, Integer> timeMinMap = new LinkedHashMap<Date, Integer>();

        int addMinuteTime = 5;
        Date temp1;
        try {
            temp1 = simpleDateFormat.parse("00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 288; i++) { // 12*24
            timeMinMap.put(temp1, 0);
            temp1 = addMinutesToDate(addMinuteTime, temp1);
        }

        for (Map.Entry<Date, Integer> entry : timeMinMap.entrySet()) {
            Date key = entry.getKey();
            if (calendar1.getLimits().getStart().compareTo(key) > 0 || calendar1.getLimits().getEnd().compareTo(key) <= 0) {
                timeMinMap.put(key, 1);
            }
            if (calendar2.getLimits().getStart().compareTo(key) > 0 || calendar2.getLimits().getEnd().compareTo(key) <= 0) {
                timeMinMap.put(key, 1);
            }
        }

        for (Time t1 : calendar1.getBookedTime()) {
            for (Map.Entry<Date, Integer> entry : timeMinMap.entrySet()) {
                Date key = entry.getKey();
                if (t1.getStart().compareTo(key) <= 0 && t1.getEnd().compareTo(key) > 0) {
                    timeMinMap.put(key, 1);
                }
            }
        }
        for (Time t2 : calendar2.getBookedTime()) {
            for (Map.Entry<Date, Integer> entry : timeMinMap.entrySet()) {
                Date key = entry.getKey();
                if (t2.getStart().compareTo(key) <= 0 && t2.getEnd().compareTo(key) > 0) {
                    timeMinMap.put(key, 1);
                }
            }
        }

        String patternHH = "HH";
        String patternMM = "mm";

        SimpleDateFormat simpleDateFormatHH = new SimpleDateFormat(patternHH);
        SimpleDateFormat simpleDateFormatMM = new SimpleDateFormat(patternMM);

        String ore = simpleDateFormatHH.format(meetingTimeMin);
        String minute = simpleDateFormatMM.format(meetingTimeMin);

        int valOK = 0;
        int current_hours = Integer.parseInt(ore);
        int current_minutes = Integer.parseInt(minute);

        int allMinutes = current_hours * 60 + current_minutes;
        Date start = null, end = null;
        for (Map.Entry<Date, Integer> entry : timeMinMap.entrySet()) {

            Integer value = entry.getValue();
            if (value == 0 && valOK == 0) {
                start = entry.getKey();
                valOK++;
            }
            if (value == 0 && valOK > 0) {
                valOK++;
            }
            if (value == 1 && valOK >= allMinutes / 5) {
                end = entry.getKey();
                String s1 = simpleDateFormat.format(start);
                String s2 = simpleDateFormat.format(end);
                Time temp = new Time(s1, s2);
                availableTime.add(temp);
                valOK = 0;
            }
            if (value == 1 && valOK < allMinutes / 5) {
                valOK = 0;
            }
        }

        /*for (Map.Entry me : timeMinMap.entrySet()) {
            System.out.println("Key: " + me.getKey() + " & Value: " + me.getValue());
        }*/

        return availableTime;
    }

    public static void main(String[] args) {

        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Time time1 = new Time("9:00", "10:30");
        Time time2 = new Time("12:00", "13:00");
        Time time3 = new Time("16:00", "18:00");
        Time limit1 = new Time("9:00", "20:00");
        ArrayList<Time> booked1 = new ArrayList<>();
        booked1.add(time1);
        booked1.add(time2);
        booked1.add(time3);
        Calendar calendar1 = new Calendar(booked1, limit1);

        Time time4 = new Time("10:00", "11:30");
        Time time5 = new Time("12:30", "14:30");
        Time time6 = new Time("14:30", "15:00");
        Time time7 = new Time("16:00", "17:00");
        Time limit2 = new Time("10:00", "18:30");
        ArrayList<Time> booked2 = new ArrayList<>();
        booked2.add(time4);
        booked2.add(time5);
        booked2.add(time6);
        booked2.add(time7);
        Calendar calendar2 = new Calendar(booked2, limit2);

        System.out.println(calendar1);
        System.out.println(calendar2);

        Date meetingTimeMin;
        try {
            meetingTimeMin = simpleDateFormat.parse("01:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Time> meets = meetings(calendar1, calendar2, meetingTimeMin);

        for (Time t : meets) {
            System.out.println(t);
        }
    }
}