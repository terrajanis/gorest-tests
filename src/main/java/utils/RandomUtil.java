package utils;

import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RandomUtil {

    public static String getRandomEmail() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
        String date = LocalDateTime.now().format(formatter);
        return String.format("tests%s%s@gmail.com", date, RandomUtils.nextInt(1, 100));
    }
}
