package cerebrik.com.alarmapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import cerebrik.com.alarmapp.R;
import cerebrik.com.alarmapp.constant.AppConstants;

public class AlarmService extends Service {
    private final String TAG = "AlarmService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (intent == null) {
            Log.d(TAG, "The intent is null.");
            return START_REDELIVER_INTENT;
        }
        displayNotification(this);
        return START_NOT_STICKY;
    }

    private void displayNotification(Context context) {
        String title, content;
        title = context.getResources().getString(R.string.app_name);
        content = context.getResources().getString(R.string.notificationMessage);

        final long[] vibratePattern = new long[] {500, 1000, 500, 1250, 500, 1500, 500, 1750, 500, 2000};
        //final Uri sound = Uri.parse(context.getString(R.string.resourceUrl) + R.raw.new_run_sound);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getPackageName() + "/" + R.raw.new_run_sound);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
            bigTextStyle.setBigContentTitle(title);
            bigTextStyle.bigText(content);

            // Oreo+ versions uses "notification" builder
            Notification.Builder notificationBuilder = new Notification.Builder(context, context.getResources().getString(R.string.notifyChannelID))
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setWhen(System.currentTimeMillis())
                    .setColorized(true)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setAutoCancel(true)
                    .setStyle(bigTextStyle)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(context.getResources().getString(R.string.notifyChannelID));

            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_ALARM).build();

            NotificationChannel notificationChannel = new NotificationChannel(context.getResources().getString(R.string.notifyChannelID), context.getResources().getString(R.string.app_channel), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(sound, audioAttributes);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(vibratePattern);

            notificationManager.createNotificationChannel(notificationChannel);

            Notification notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_INSISTENT;

            notificationManager.notify(AppConstants.ALARM_JOB_ID, notification);
        } else {
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(title);
            bigTextStyle.bigText(content);

            // Pre Oreo versions uses "notification compat" builder
            @SuppressWarnings("deprecation")
            NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(context)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setWhen(System.currentTimeMillis())
                    .setColorized(true)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setVibrate(vibratePattern)
                    .setSound(sound)
                    .setAutoCancel(true)
                    .setStyle(bigTextStyle)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
                    .setChannelId(context.getResources().getString(R.string.notifyChannelID));

            Notification notification = notificationCompatBuilder.build();
            notification.flags |= NotificationCompat.FLAG_INSISTENT;

            notificationManager.notify(AppConstants.ALARM_JOB_ID, notification);
        }
    }
}
