package io.github.loopX.XAlarm.database;

/**
 * Author SunMeng
 * Date : 2016 七月 25
 */
public class AlarmTable {
    
    
    public static final String NAME = "tb_xalarm";

    public static final class Columns {

        public static final String UUID = "uuid";
        public static final String HOUR = "hour";
        public static final String MINUTE = "minute";
        public static final String UNLOCK_TYPE = "unlock_type";
        public static final String DAYS = "days";
        public static final String TONE = "alarm_tone";
        public static final String ENABLED = "enabled";
        public static final String VIBRATE = "vibrate";

    }

}
