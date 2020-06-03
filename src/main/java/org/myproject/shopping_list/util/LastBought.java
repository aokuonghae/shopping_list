package org.myproject.shopping_list.util;

import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LastBought {

    public static LocalDateTime getLastBought() {
        return LocalDateTime.now();
    }

    public static String convertLastBought(LocalDateTime date) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
        return formatter1.format(date);
    }

}
