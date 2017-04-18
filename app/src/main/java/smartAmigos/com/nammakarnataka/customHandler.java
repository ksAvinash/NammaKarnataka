package smartAmigos.com.nammakarnataka;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.pushbots.push.PBNotificationIntent;
import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;


public class customHandler extends BroadcastReceiver {
    private static final String TAG = "customHandler";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action=" + action);


        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {


            Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_OPEN);
            Pushbots.PushNotificationOpened(context, bundle);
            Log.i(TAG, "User clicked notification with Message: " + bundle.get("message"));


            //Clear Notification array
            if (PBNotificationIntent.notificationsArray != null) {
                PBNotificationIntent.notificationsArray = null;
            }



            Intent resultIntent = new Intent(context, NotifHandler.class);

            resultIntent.putExtras(intent.getBundleExtra("pushData"));

            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(resultIntent);


        } else if(action.equals(PBConstants.EVENT_MSG_RECEIVE)){

            //Bundle containing all fields of the notification
            Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_RECEIVE);
            Log.i(TAG, "User received notification with Message: " + bundle.get("message"));

        }

    }
}