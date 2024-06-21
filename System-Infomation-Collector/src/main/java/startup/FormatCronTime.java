package startup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormatCronTime {

    private static final Map<String, String> DAYS_OF_WEEK = new HashMap<>();
    private static final Map<String, String> MONTHS = new HashMap<>();

    static {
        DAYS_OF_WEEK.put("0", "Sunday");
        DAYS_OF_WEEK.put("1", "Monday");
        DAYS_OF_WEEK.put("2", "Tuesday");
        DAYS_OF_WEEK.put("3", "Wednesday");
        DAYS_OF_WEEK.put("4", "Thursday");
        DAYS_OF_WEEK.put("5", "Friday");
        DAYS_OF_WEEK.put("6", "Saturday");
        DAYS_OF_WEEK.put("7", "Sunday");

        MONTHS.put("1", "January");
        MONTHS.put("2", "February");
        MONTHS.put("3", "March");
        MONTHS.put("4", "April");
        MONTHS.put("5", "May");
        MONTHS.put("6", "June");
        MONTHS.put("7", "July");
        MONTHS.put("8", "August");
        MONTHS.put("9", "September");
        MONTHS.put("10", "October");
        MONTHS.put("11", "November");
        MONTHS.put("12", "December");
    }

    public static void main(String[] args) {
        String cronExpression = "17 6 5 * *";
        try {
            String readable = convertCronToReadable(cronExpression);
            System.out.println(readable);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String convertCronToReadable(String cronExpression) throws ParseException {
        String[] parts = cronExpression.split(" ");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid cron expression");
        }

        String minutes = parts[0];
        String hours = parts[1];
        String dayOfMonth = parts[2];
        String month = parts[3];
        String dayOfWeek = parts[4];

        String readableTime = convertTime(hours, minutes);
        String readableDayOfWeek = convertDayOfWeek(dayOfWeek);
        String readableMonth = convertMonth(month);
        String readableDayOfMonth = convertDayOfMonth(dayOfMonth);

        return String.format("%s %s %s %s", readableTime, readableDayOfWeek, readableMonth, readableDayOfMonth).trim();
    }

    private static String convertTime(String hours, String minutes) throws ParseException {
        if (hours.equals("*") && minutes.equals("*")) {
            return "every minute";
        }
        if (hours.equals("*")) {
            return String.format("every hour at %s minutes", minutes);
        }
        if (minutes.equals("*")) {
            return String.format("%s minutes past every hour", hours);
        }

        SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf12 = new SimpleDateFormat("h:mm a");

        Date date = sdf24.parse(hours + ":" + minutes);
        return sdf12.format(date);
    }

    private static String convertDayOfWeek(String dayOfWeek) {
        if (dayOfWeek.equals("*")) {
            return "";
        }
        return "every " + DAYS_OF_WEEK.get(dayOfWeek);
    }

    private static String convertMonth(String month) {
        if (month.equals("*")) {
            return "";
        }
        return "in " + MONTHS.get(month);
    }

    private static String convertDayOfMonth(String dayOfMonth) {
        if (dayOfMonth.equals("*")) {
            return "";
        }
        return "on day " + dayOfMonth;
    }
}
