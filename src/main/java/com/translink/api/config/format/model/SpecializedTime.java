package com.translink.api.config.format.model;

import org.jetbrains.annotations.NotNull;

import java.text.ParsePosition;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

// Source: https://stackoverflow.com/a/65484682/14148807
public class SpecializedTime implements Comparable<SpecializedTime> {
    private final Duration timeSince0000;

    private SpecializedTime(Duration timeSince0000) {
        this.timeSince0000 = timeSince0000;
    }

    public static SpecializedTime parse(String text, DateTimeFormatter formatter) {
        ParsePosition position = new ParsePosition(0);
        TemporalAccessor parsed = formatter.parseUnresolved(text, position);
        if (position.getErrorIndex() != -1) {
            throw new DateTimeParseException("Parse error", text, position.getErrorIndex());
        }
        if (position.getIndex() != text.length()) {
            throw new DateTimeParseException("Unparsed text", text, position.getIndex());
        }
        if (!parsed.isSupported(ChronoField.HOUR_OF_DAY)) {
            throw new DateTimeParseException("Cannot resolve", text, 0);
        }

        Duration time = Duration.ofHours(parsed.getLong(ChronoField.HOUR_OF_DAY));
        if (parsed.isSupported(ChronoField.MINUTE_OF_HOUR)) {
            int minuteOfHour = parsed.get(ChronoField.MINUTE_OF_HOUR);
            // Should validate, 0..59
            time = time.plusMinutes(minuteOfHour);
        }
        if (parsed.isSupported(ChronoField.SECOND_OF_MINUTE)) {
            int secondOfMinute = parsed.get(ChronoField.SECOND_OF_MINUTE);
            // Should validate, 0..59
            time = time.plusSeconds(secondOfMinute);
        }

        return new SpecializedTime(time);
    }

    @Override
    public String toString() {
        long seconds = timeSince0000.getSeconds();
        long hours = seconds / 3600;
        int minutes = (int) ((seconds % 3600) / 60);
        int secs = (int) (seconds % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    @Override
    public int compareTo(@NotNull SpecializedTime o) {
        return this.timeSince0000.compareTo(o.timeSince0000);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SpecializedTime) {
            SpecializedTime time = (SpecializedTime) obj;

            return this.timeSince0000.equals(time.timeSince0000);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.timeSince0000.hashCode();
    }
}
