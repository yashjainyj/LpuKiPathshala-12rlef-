package com.example.lpukipathshala.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.lpukipathshala.Cart.Cart;
import com.example.lpukipathshala.Cart.Chat_Dsiplay;
import com.example.lpukipathshala.Login_Fragment.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.constraint.Constraints.TAG;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.i(TAG, "onMessageReceived: ---------------------------------------"+remoteMessage.getData().get("sented"));
//        Log.i(TAG, "onMessageReceived: ---------------------------------------"+FirebaseAuth.getInstance().getUid());
        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null && sented.equals(firebaseUser.getUid()))
        {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            {
                sendOreoNotifcation(remoteMessage);
            }
            else
            sendNotifcation(remoteMessage);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOreoNotifcation(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
//         Log.i("asdfghjkl;kjhgfdsa", "sendNotifcation:---------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ remoteMessage.getData().get("user"));
//        Log.i(TAG, "sendOreoNotifcation: " + user);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent =  new Intent(this, Cart.class);
        intent.putExtra("u_id",remoteMessage.getData().get("user"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getNotification(title,body,pendingIntent,defaultSound,icon);
        int i=0;
        if(j>0){
            i=j;
        }
        oreoNotification.getNotificationManager().notify(i,builder.build());
    }

    private void sendNotifcation(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        Log.i("asdfghjkl;kjhgfdsa", "sendNotifcation:---------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ remoteMessage.getData().get("user"));
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent =  new Intent(this, Cart.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userid",user);
        intent.putExtra("u_id",remoteMessage.getData().get("user"));
        //bundle.putString("receiver_id",remoteMessage.getData().get("sented"));
        //intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(Integer.parseInt(icon)).setContentTitle(title).setContentText(body).setAutoCancel(true).setSound(defaultSound).setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int i=0;
        if(j>0){
            i=j;
        }
    notificationManager.notify(i,builder.build());
    }
}