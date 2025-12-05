package hamdizeyneb.grp1.findmyfriends;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MysmsReceiver extends BroadcastReceiver {


    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MysmsReceiver", "onReceive called, action=" + intent.getAction());
// TODO: This method is called when the BroadcastReceiver is receiving an
// Intent broadcast.
        String messageBody,phoneNumber;
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle =intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length >-1) {
                    messageBody = messages[0].getMessageBody();
                    phoneNumber = messages[0].getDisplayOriginatingAddress();

                   if(messageBody.startsWith("Find Friend"))
                   {
                       System.out.println("you are here");
                       Intent i=new Intent(context,MyGeoService.class);
                       i.putExtra("sender",phoneNumber);
                       context.startService(i);
                   }
                   if(messageBody.startsWith("Findfriends:mylocationis"))
                   {
                       String[]t=messageBody.split("#");
                       String longitude=t[1];
                       String latitude=t[2];

                       //lancer notification
                       //on doit creer channelid
                       NotificationCompat.Builder mynotif=new NotificationCompat.Builder(context,"FindFriends_channelID");
                       mynotif.setContentTitle("position reçu");
                       mynotif.setContentText("appyuer pour voir la position sur le map");
                       mynotif.setSmallIcon(android.R.drawable.ic_dialog_map);
                       mynotif.setAutoCancel(true);

                       Intent i=new Intent(context,MapsActivity.class);
                       i.putExtra("longitude",longitude);
                       i.putExtra("latitude",latitude);
                       PendingIntent pi=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_MUTABLE);
                       mynotif.setContentIntent(pi);


                       NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);

                       NotificationChannel  canal=new NotificationChannel("FindFriends_channelID","canal pour find friends", NotificationManager.IMPORTANCE_HIGH);
                       notificationManagerCompat.createNotificationChannel(canal);

                       notificationManagerCompat.notify(0,mynotif.build());

                   }

                }
            }
        }
    }
}