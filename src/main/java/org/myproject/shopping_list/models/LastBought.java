package org.myproject.shopping_list.models;

import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LastBought {

    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = LocalDateTime.now();
    }

    public static LocalDateTime getLastBought() {
//        LocalDateTime calendarDate= LocalDateTime.now();
//        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
//        formatter1.format(calendarDate);
        return LocalDateTime.now();
    }

}
