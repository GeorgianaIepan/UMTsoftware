import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static final long ONE_MINUTE_IN_MILLIS = 60000;  // valoarea unui minut in milisecunde
    static String pattern = "HH:mm";  // formatul tipului Date: "ore:minute"
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    /**
     * metoda pentru adaugarea minutelor intr-o variabila de tip Date
     * @param minutes - minutele pe care vrem sa le adaugam
     * @param beforeTime -  data pe care vrem sa o modificam
     * @return - returneaza noul Date cu minutele adaugate
     */
    public static Date addMinutesToDate(int minutes, Date beforeTime) {
        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    /**
     * metoda care cauta toate intervalele posibile in care doua persoane se pot intalni in functie de calendarele lor si durata dorita
     * @param calendar1 - calendarul primei persoane
     * @param calendar2 - calendarul celei de-a doua persoane
     * @param meetingTime - minutele/durata intalnirii
     * @return - returneaza o lista a posibilelor intervale disponibile pentru intalnire
     */
    public static ArrayList<Time> meetings(Calendar calendar1, Calendar calendar2, String meetingTime) {
        ArrayList<Time> availableTime = new ArrayList<>();  // lista intervalele posibile de intalnire

        Date meetingTimeMin;
        try {
            meetingTimeMin = simpleDateFormat.parse(meetingTime);  // durata intalnirii dorite
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        LinkedHashMap<Date, Integer> timeMinMap = new LinkedHashMap<Date, Integer>();  // map - cheie = ora, cu frecventa data de 5 min,
                                                                           // valoare = ora e sau nu disponibila pentru intalnire 1=ocupat, 0= liber
        int addMinuteTime = 5;  // intervalul dupa care se cauta disponibilitatea
        Date temp1;
        try {
            temp1 = simpleDateFormat.parse("00:00");  // ora initiala pentru popularea map-ului
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < (60/addMinuteTime)*24; i++) { // popularea map-ului cu valori pana la 24 de ore, de la "00:00" la "23:55"
            timeMinMap.put(temp1, 0);
            temp1 = addMinutesToDate(addMinuteTime, temp1);  // apelarea functiei de incrementare
        }

        // verificarea limitelor pentru intalniri
        // popularea map-ului cu valori de 1 pentru cheile ocupate
        for (Map.Entry<Date, Integer> entry : timeMinMap.entrySet()) {
            Date key = entry.getKey();
            if (calendar1.getLimits().getStart().compareTo(key) > 0 || calendar1.getLimits().getEnd().compareTo(key) <= 0) {
                timeMinMap.put(key, 1);
            }
            if (calendar2.getLimits().getStart().compareTo(key) > 0 || calendar2.getLimits().getEnd().compareTo(key) <= 0) {
                timeMinMap.put(key, 1);
            }
        }

        // parcurgerea intervalelor ocupate din calendar1
        // popularea map-ului cu valori de 1 pentru cheile ocupate
        for (Time t1 : calendar1.getBookedTime()) {
            for (Map.Entry<Date, Integer> entry : timeMinMap.entrySet()) {
                Date key = entry.getKey();
                if (t1.getStart().compareTo(key) <= 0 && t1.getEnd().compareTo(key) > 0) {
                    timeMinMap.put(key, 1);
                }
            }
        }

        // parcurgerea intervalelor ocupate din calendar2
        // popularea map-ului cu valori de 1 pentru cheile ocupate
        for (Time t2 : calendar2.getBookedTime()) {
            for (Map.Entry<Date, Integer> entry : timeMinMap.entrySet()) {
                Date key = entry.getKey();
                if (t2.getStart().compareTo(key) <= 0 && t2.getEnd().compareTo(key) > 0) {
                    timeMinMap.put(key, 1);
                }
            }
        }

        String patternHH = "HH";  // format de ora
        String patternMM = "mm";  // format de minute

        SimpleDateFormat simpleDateFormatHH = new SimpleDateFormat(patternHH);
        SimpleDateFormat simpleDateFormatMM = new SimpleDateFormat(patternMM);

        String ore = simpleDateFormatHH.format(meetingTimeMin);     // transformare ore in String
        String minute = simpleDateFormatMM.format(meetingTimeMin);  // transformare minute in string

        int valOK = 0;
        int current_hours = Integer.parseInt(ore);       // transformare ore in int
        int current_minutes = Integer.parseInt(minute);  // transformare minute in int

        int allMinutes = current_hours * 60 + current_minutes;  // calcularea duratei unei intalniri in minute
        Date start = null, end = null;  // variabile temporare pentru formarea intervalelor

        for (Map.Entry<Date, Integer> entry : timeMinMap.entrySet()) {  // parcurgerea map-ului cu valori setate pentru a gasi intervalele disponibile
            Integer value = entry.getValue();
            if (value == 0 && valOK == 0) {  // daca valoarea din map e 0 - perioada disponibila pentru ambele persoane
                start = entry.getKey();
                valOK++;
            }
            if (value == 0 && valOK > 0) {
                valOK++;  // se cauta un interval disponibil egal sau mai mare cu durata ceruta
            }
            if (value == 1 && valOK >= allMinutes / addMinuteTime) {  // daca valoarea din map e 1 - perioada nu e disponibila pentru ambele persoane
                end = entry.getKey();                                 // se gaseste un interval disponibil egal sau mai mare cu durata ceruta
                String s1 = simpleDateFormat.format(start);
                String s2 = simpleDateFormat.format(end);
                Time temp = new Time(s1, s2);
                availableTime.add(temp);  // se salveaza intervalul bun gasit in lista de timp disponibil pentru intalnire
                valOK = 0;  // se reseteaza cautarea
            }
            if (value == 1 && valOK < allMinutes / addMinuteTime) {  // s-a gasit un interval, nu suficient de lung
                valOK = 0;  // se reseteaza cautarea
            }
        }

        return availableTime;
    }

    public static void main(String[] args) {

        Time time1 = new Time("9:00", "10:30");  // calendar 1
        Time time2 = new Time("12:00", "13:00");
        Time time3 = new Time("16:00", "18:00");
        Time limit1 = new Time("9:00", "20:00");
        ArrayList<Time> booked1 = new ArrayList<>();
        booked1.add(time1);
        booked1.add(time2);
        booked1.add(time3);
        Calendar calendar1 = new Calendar(booked1, limit1);

        Time time4 = new Time("10:00", "11:30");  // calendar 2
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

        String meetingTimeMin = "00:30";  // durata intalnirii dorite in formatul "HH:mm"

        ArrayList<Time> meets = meetings(calendar1, calendar2, meetingTimeMin);  // apelarea functiei pentru gasirea intervalelor disponibile

        for (Time t : meets) {
            System.out.println(t);  // afisare rezultat
        }
    }
}