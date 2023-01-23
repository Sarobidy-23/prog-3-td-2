package app.foot.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


public class DateUtils {
    public String parseDate(Instant instant) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        return formatter.format(instant);
    }
}
