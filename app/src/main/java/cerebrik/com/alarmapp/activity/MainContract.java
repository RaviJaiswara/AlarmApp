package cerebrik.com.alarmapp.activity;

import android.widget.TimePicker;

public interface MainContract {
    interface Views{
        void onSetAlarm(long alarmTime, long currentTime);
    }
    interface Presenters{
        void setAlarm(TimePicker timePicker);
    }
}
