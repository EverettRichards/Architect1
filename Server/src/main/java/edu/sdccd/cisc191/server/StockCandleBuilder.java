package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.common.entities.StockCandle;

import java.net.MalformedURLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

public class StockCandleBuilder {
    // getTimeRange returns the start/end time stamps used when searching for a given stock candle chart
    public static long[] getTimeRange(String duration){ // duration can be day, week, month, 6month, year

        ZoneId estZoneId = ZoneId.of("America/New_York");
        LocalDateTime now = LocalDateTime.now(estZoneId);
        DayOfWeek currentDayOfWeek = now.getDayOfWeek();
        LocalDateTime targetDateTime = now;

        if (duration.equals("day")) {
            if (currentDayOfWeek == DayOfWeek.SATURDAY || currentDayOfWeek == DayOfWeek.SUNDAY) {
                targetDateTime = now.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
            } else {
                targetDateTime = now;
            }
        } else if (duration.equals("week")) {
            if (currentDayOfWeek != DayOfWeek.MONDAY) {
                targetDateTime = now.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            } else {
                targetDateTime = now;
            }
        } else if (duration.equals("month")) {
            targetDateTime = targetDateTime.minusDays(30);
        } else if (duration.equals("6month")) {
            targetDateTime = targetDateTime.minusDays(183);
        } else if (duration.equals("year")) {
            targetDateTime = targetDateTime.minusDays(365);
        } else if (duration.equals("5year")) {
            targetDateTime = targetDateTime.minusDays(365*5);
        }

        long additionalTime = switch (duration) {
            case "day" -> 60*60*16; // 4:00PM
            case "week" -> 60*60*16 + 60*60*24*4; // Assume 5 days (M-F)
            case "month" -> 60*60*16 + 60*60*24*29; // Assume 30 days
            case "6month" -> 60*60*16 + 60*60*24*(182); // Assume 183 days
            case "year" -> 60*60*16 + 60*60*24*(364); // Assume 365 days
            case "5year" -> 60*60*16 + 60*60*24*(365*5); // Assume 365*5+1 days
            default -> 60*60*16;
        };

        targetDateTime = targetDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        long baseTime = targetDateTime.atZone(estZoneId).toInstant().toEpochMilli()/1000;
        long startTime = baseTime + (long)(60*60*9.5); // Opening time is always 9:30AM Eastern
        long endTime = baseTime + additionalTime;

        long[] stamps = new long[2];
        stamps[0] = startTime;
        stamps[1] = endTime;
        return stamps;
    }

    public static String getFrequency(String duration){
        String frequency = switch (duration) {
            case "day" -> "5";
            case "week" -> "15";
            case "month" -> "60";
            case "6month" -> "D";
            case "year" -> "D";
            case "5year" -> "W";
            default -> "5";
        };
        return frequency;
    }

    public static ServerStockCandle newStockCandle(String newTicker, String resolution, long time1, long time2) throws MalformedURLException, JsonProcessingException {
        ServerStockCandle candle = new ServerStockCandle(newTicker,"day");
        return candle;
    }

    public static ServerStockCandle newStockCandle(String newTicker) throws MalformedURLException, JsonProcessingException {
        long[] timeRange = getTimeRange("day");
        long time1 = timeRange[0];
        long time2 = timeRange[1];
        return newStockCandle(newTicker,getFrequency("day"),time1,time2);
    }

    public static void UpdateCandle(StockCandle candle, String newTicker, String duration) throws MalformedURLException, JsonProcessingException {
        long[] timeRange = getTimeRange(duration);
        String frequency = getFrequency(duration);
        //FinnhubNetworking.UpdateCandle(candle, newTicker,frequency, timeRange[0],timeRange[1]);
    }
    public static void UpdateCandle(StockCandle candle, String newTicker) throws MalformedURLException, JsonProcessingException {
        UpdateCandle(candle, newTicker,"5year");
    }
}
