package com.donvigo.GettingThingsDone.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * @param initialDate
     * @param days Days for adding to initialDate
     * @return date in short format - ddMM.
     */
    public static String getShortDateString(Date initialDate, int days){
        SimpleDateFormat sdf = new SimpleDateFormat("ddMM");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initialDate);
        calendar.add(Calendar.DATE, days);

        return sdf.format(calendar.getTime());
    }
}
