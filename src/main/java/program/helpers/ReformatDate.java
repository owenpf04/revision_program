package program.helpers;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReformatDate {
    public static String describeInWords(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Period datesBetween = Period.between(dateTime.toLocalDate(), currentDateTime.toLocalDate());

        if (ChronoUnit.DECADES.between(dateTime, currentDateTime) > 0) {
            return "Over a decade ago";
        }

        if (datesBetween.getYears() == 1) {
            return "A year ago";
        } else if (datesBetween.getYears() > 1) {
            return (datesBetween.getYears() + " years ago");
        }

        if (datesBetween.getMonths() == 1) {
            return "A month ago";
        } else if (datesBetween.getMonths() > 1) {
            return (datesBetween.getMonths() + " months ago");
        }

        if (datesBetween.getDays() >= 14) {
            return ((datesBetween.getDays() / 2) + " weeks ago");
        }


        String timeOfDay = getTimeOfDay(dateTime, true);
        if (datesBetween.getDays() >= 7){
            return (datesBetween.getDays() + " days ago");
        } else if (datesBetween.getDays() > 1) {
            return (ReformatString.toCamelCase(dateTime.getDayOfWeek().name()) + " " + timeOfDay);
        } else if (datesBetween.getDays() == 1) {
            if (timeOfDay.equals("night")) {
                return "Last night";
            } else {
                return ("Yesterday " + timeOfDay);
            }
        }

        long hoursBetween = ChronoUnit.HOURS.between(dateTime, currentDateTime);
        if (!timeOfDay.equals(getTimeOfDay(currentDateTime, true)) && hoursBetween >= 5) {
            return ("This " + timeOfDay);
        } else if (hoursBetween == 1) {
            return "An hour ago";
        } else if (hoursBetween > 1) {
            return (hoursBetween + " hours ago");
        }

        long minsBetween = ChronoUnit.MINUTES.between(dateTime, currentDateTime);
        if (minsBetween == 1) {
            return "A minute ago";
        } else if (minsBetween > 1) {
            return (minsBetween + " minutes ago");
        } else if (minsBetween == 0){
            return "Just now";
        }

        return "In the future...?";
    }

    public static String getTimeOfDay(LocalDateTime dateTime, boolean includeNight) {
        int hour = dateTime.getHour();
        if (hour >= 4 && hour < 12) {
            return "morning";
        } else if (hour >= 12 && hour < 17) {
            return "afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "evening";
        } else {
            if (includeNight) {
                return "night";
            } else {
                return "evening";
            }
        }
    }

    public static String formatAppropriately(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String dayNumSuffix = "";
        DateTimeFormatter formatter;

        switch (dateTime.getDayOfMonth() % 10) {
            case 1 -> {
                dayNumSuffix = "st";
            }
            case 2 -> {
                dayNumSuffix = "nd";
            }
            case 3 -> {
                dayNumSuffix = "rd";
            }
            default -> {
                dayNumSuffix = "th";
            }
        }

        if (ChronoUnit.DAYS.between(dateTime, currentDateTime) == 0) {
            formatter = DateTimeFormatter.ofPattern("HH:mm");
        } else {
            formatter = DateTimeFormatter.ofPattern("d'" + dayNumSuffix + "' MMMM yyyy");
        }

        return dateTime.format(formatter);
    }
}
