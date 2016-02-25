package com.mirhoseini.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

public class SmsSender {
	Context context;
	String callCenter;

	public interface smsListener {

		public void OnSending();

		public void OnSent();

		public void OnNotSent();

		public void OnDelivered();

		public void OnNotDelivered();
	}

	/** Called when the activity is first created. */
	public SmsSender(Context c, String cCenter) {
		context = c;
		callCenter = cCenter;
	}

	// ---sends a SMS message to another device---
	public void sendSMS(String message, final smsListener smsListner) {

		SmsManager sms = SmsManager.getDefault();

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		// ---when the SMS has been sent---
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					if (smsListner != null)
						smsListner.OnSent();

					break;
				default:
					if (smsListner != null)
						smsListner.OnNotSent();
					break;
				}
				c.unregisterReceiver(this);
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					if (smsListner != null)
						smsListner.OnDelivered();
					break;
				default:
					if (smsListner != null)
						smsListner.OnNotDelivered();
					break;

				}
				c.unregisterReceiver(this);
			}
		}, new IntentFilter(DELIVERED));

		if (smsListner != null)
			smsListner.OnSending();

		ArrayList<String> msgStringArray = sms.divideMessage(message);
		int msgCount = msgStringArray.size();

		ArrayList<PendingIntent> sentPI = new ArrayList<PendingIntent>(msgCount);

		ArrayList<PendingIntent> deliveredPI = new ArrayList<PendingIntent>(
				msgCount);

		for (int i = 0; i < msgCount; i++) {
			sentPI.add(PendingIntent.getBroadcast(context, 0, new Intent(SENT),
					0));
			deliveredPI.add(PendingIntent.getBroadcast(context, 0, new Intent(
					DELIVERED), 0));
		}

		sms.sendMultipartTextMessage(callCenter, null, msgStringArray, sentPI,
				deliveredPI);

	}

}