package com.study.test.date;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateTest01 {
    @Test
    public void test01() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("2019-01-02", formatter);
        LocalDate endDate = LocalDate.parse("2020-01-01", formatter);
        // 日期区间
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        //月
        long month = ChronoUnit.MONTHS.between(startDate, endDate);
        //年
        long year = ChronoUnit.YEARS.between(startDate, endDate);
        System.out.println(year + ", " + month + ", " + days);
    }

    @Test
    public void test02() {
        // 当前日期的前一天：昨天
        LocalDate start = LocalDate.of(2020, 11, 16);
        LocalDate end = LocalDate.of(2020, 11, 20);
        while (start.isBefore(end)) {
            System.out.println(start);
            start = start.plusDays(1);
        }
        System.out.println(end.isBefore(end));
        System.out.println(end.equals(end));
        System.out.println(end.isAfter(end));
    }
}
