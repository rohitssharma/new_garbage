package garbagebin.com.garbagebin;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;


public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	String TAG = "Log";
	Context c;
	public GcmIntentService() {
		super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		Log.d("response_extras_test ", extras + "123");

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		c = (Context)getApplicationContext();

		/*if (!extras.isEmpty()) {*/

		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			sendNotification("Send error:" + extras.toString());
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			sendNotification("Deleted messages on server: "
					+ extras.toString());
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

			// Bundle[{data={"message":"We are creating an online marketplace which will connect transport vehicle owners and customers seeking to transport goods (Business or household) in a quick and hassle free ProfileActivity Android tesr36573"}, from=1009326785022, android.support.content.wakelockid=2, collapse_key=do_not_collapse}]

			String res = intent.getExtras().getString("data").toString();
			String message = "";

			try {
				JSONObject jsonObject = new JSONObject(res);
				message = jsonObject.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			//find the home launcher Package
			Intent inttent = new Intent(Intent.ACTION_MAIN);
			inttent.addCategory(Intent.CATEGORY_HOME);
			ResolveInfo resolveInfo = getPackageManager().resolveActivity(inttent, PackageManager.MATCH_DEFAULT_ONLY);
			String currentHomePackage = resolveInfo.activityInfo.packageName;
			Toast.makeText(getApplicationContext(), "launcher:" + currentHomePackage, Toast.LENGTH_SHORT).show();
			//Log.i(TAG, "Message: " + extras.toString());

			sendNotification(message);


		}
		//}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String notification_msg) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		{
			/*Intent intent = new Intent(c, Info.class);
			intent.putExtra("PUSH_MESSAGE",notification_msg);
			CommonControl.notification_msg = notification_msg;
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);*/

			//PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, 0);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(getResources().getString(R.string.app_name))
					.setAutoCancel(true)
					.setStyle(new NotificationCompat.BigTextStyle().bigText(notification_msg))
						/* .setSound(Uri.parse("android.resource://"
						             +c.getPackageName() + "/" + R.raw.sound1))*/
					.setContentText(notification_msg);
			Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(500);
			//mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		}
	}
}

