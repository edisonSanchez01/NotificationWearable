package com.edisondeveloper.petagram.Modelo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.edisondeveloper.petagram.R;
import com.edisondeveloper.petagram.Vista.MainActivity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, MainActivity.class);
        Intent follow = new Intent();
        follow.setAction(Constantes.KEY_ACTION);
        PendingIntent pendingIntentFollow = PendingIntent.getBroadcast(this, 0, follow, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Uri uriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "")
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_heart)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.home, getString(R.string.action_perfil), pendingIntent)
                .addAction(R.drawable.user, getString(R.string.action_user), pendingIntent)
                .addAction(R.drawable.follow, getString(R.string.action_follow), pendingIntentFollow)
                .setSound(uriSound);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());

    }


}
