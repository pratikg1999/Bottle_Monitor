package com.example.bottle_monitor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationHelper {
    final static private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final static private String serverKey = "key=" + "AIzaSyBQIukFQADU5EHYi245TRen9_WzPmVAmYs";
//    final static private String contentType = "application/json";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static final String CHANNEL_ID = NavActivity.CHANNEL_ID;
    public static void dispNotification(Context ctx, String title, String message){
        final Intent intent = new Intent(ctx, NavActivity.class);
        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(ctx.getResources(),
                R.drawable.ic_notifications_black_24dp);
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);
        notificationManager.notify(notificationID ,notificationBuilder.build());
    }

    public static void sendNotification(Context ctx, final String title, final String message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();

                try {
                    OkHttpClient client = new OkHttpClient();
                    notifcationBody.put("title", title);
                    notifcationBody.put("message", message);

                    notification.put("to", TOPIC);
                    notification.put("data", notifcationBody);
                    RequestBody body = RequestBody.create(JSON, notification.toString());

                    Request request = new Request.Builder().addHeader("Authorization", serverKey)
                            .url(FCM_API)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.d("Notification", "Sent to " + TOPIC);
                    Log.d("Notification", "notification sent");
                } catch (JSONException e) {
                    Log.e("sending notification", "onCreate: " + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return  null;
            }
        }.execute();
    }
}
