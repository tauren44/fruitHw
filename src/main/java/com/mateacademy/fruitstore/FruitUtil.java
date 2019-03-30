package com.mateacademy.fruitstore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FruitUtil {
    private static final String DATE_PATTERN = "dd/MM/yy";

    public static LocalDate convertStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDate.parse(date, formatter);
    }
}
