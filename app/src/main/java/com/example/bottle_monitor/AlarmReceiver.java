package com.example.bottle_monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {
    public static Ringtone r;
    public static MediaPlayer player;
    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLocker.acquire(context);
        NotificationHelper.dispNotification(context, "Alarm received", "alarm got");

//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        r  =RingtoneManager.getRingtone(context,notification );
//        r.play();
        player = MediaPlayer.create(context, R.raw.gullyboy);
        player.start();
        Intent wakeupIntent = new Intent(context, AlarmWakeupScreen.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(wakeupIntent);
        WakeLocker.release();

    }
}
