package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RandomUtils {

    public static String getRandomEmail() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
        String date = LocalDateTime.now().format(formatter);
        return String.format("tests%s@gmail.com", date);
    }
}
