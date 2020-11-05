package com.example.lib2;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyClass {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        String time1 = "2020 11 5 12:10";
        String time2 = "2020 11 5 10:01";
        String time3 = "2020 11 6 10:01";
//        Date t1 = formatter1.parse(time1);
//        Date t2 =formatter1.parse(time2);
//        Date t3 = formatter1.parse(time3);
        System.out.println(time1.compareTo(time2));
        System.out.println(time1.compareTo(time3));
//        System.out.println(t1.before(t2));
//        System.out.println(t1.before(t3));

    }
}