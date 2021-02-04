package com.mohsenoid.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import java.util.ArrayList;

public class SmsSender {

    static final String ACTION_SENT = "SMS_SENT";
    static final String ACTION_DELIVERED = "SMS_DELIVERED";

    // ---sends a SMS message to another device---
    public static void sendSMS(final Context context, String to, String message, final com.mohsenoid.utils.SmsSender.SmsListener listener) {

        SmsManager sms = SmsManager.getDefault();

        // ---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        if (listener != null)
                            listener.onSent();

                        context.unregisterReceiver(this);
                        break;
                    default:
                        if (listener != null)
                            listener.onNotSent();

                        context.unregisterReceiver(this);
                        break;
                }
            }
        }, new IntentFilter(ACTION_SENT));

        // ---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        if (listener != null)
                            listener.onDelivered();

                        context.unregisterReceiver(this);
                        break;
                    default:
                        if (listener != null)
                            listener.onNotDelivered();

                        context.unregisterReceiver(this);
                        break;
                }
            }
        }, new IntentFilter(ACTION_DELIVERED));

        if (listener != null)
            listener.onSending();

        ArrayList<String> msgStringArray = sms.divideMessage(message);
        int msgCount = msgStringArray.size();

        ArrayList<PendingIntent> sentPI = new ArrayList<>(msgCount);

        ArrayList<PendingIntent> deliveredPI = new ArrayList<>(msgCount);

        for (int i = 0; i < msgCount; i++) {
            sentPI.add(PendingIntent.getBroadcast(context, 0, new Intent(ACTION_SENT),
                    0));
            deliveredPI.add(PendingIntent.getBroadcast(context, 0, new Intent(ACTION_DELIVERED), 0));
        }

        try {
            sms.sendMultipartTextMessage(to, null, msgStringArray, sentPI,
                    deliveredPI);
        } catch (Exception e) {
            if (listener != null)
                listener.onError(e);
        }

    }

    public interface SmsListener {

        void onSending();

        void onSent();

        void onNotSent();

        void onDelivered();

        void onNotDelivered();

        void onError(Exception e);
    }

}