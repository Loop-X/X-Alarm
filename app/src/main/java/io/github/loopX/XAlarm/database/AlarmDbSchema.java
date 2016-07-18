package io.github.loopX.XAlarm.database;

/**
 * This class defines database schema.
 */
public class AlarmDBSchema {

    /**
     * Alarm Table Schema
     */
    public static final class AlarmTable {

        public static final String NAME = "xalarm.tb.alarm";

        public static final class Columns {

            public static final String UUID            = "uuid";
            public static final String HOUR            = "hour";
            public static final String MINUTE          = "minute";
            public static final String UNLOCK_TYPE     = "unlock_type";
            public static final String DAYS            = "days";
            public static final String TONE            = "alarm_tone";
            public static final String ENABLED         = "enabled";
            public static final String VIBRATE         = "vibrate";

        }
    }

}
