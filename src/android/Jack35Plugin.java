package com.outsystems.experts.plugins.jack35;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class Jack35Plugin extends CordovaPlugin {
    private static final String TAG = "Jack35Plugin";
    private final String INVALID_ARGS_ERROR = "Invalid arguments -> ";
    private final String INVALID_ACTION_ERROR = "Invalid action";

    //PREFERENCES KEYS
    private final String ICON_RES_NAME = "ICON_RES_NAME";
    private final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    private final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";
    //PREFERENCES DEFAULT VALUES KEYS
    private final String ICON_RES_NAME_DEFVAL = "jack35Icon";
    private final String NOTIFICATION_TITLE_DEFVAL = "Audio Warning";
    private final String NOTIFICATION_MESSAGE_DEFVAL = "Jack was unplugged!";

    private JackIntentReceiver jackIntentReceiver;

    public Notification createNotification(String title, String text, int priority) {
        Context context = cordova.getActivity();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "0")
                .setSmallIcon(getImageId(context.getResources(), preferences.getString(ICON_RES_NAME, ICON_RES_NAME_DEFVAL), context.getPackageName()))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(priority);
        return mBuilder.build();
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext)
            throws JSONException {
        /* Context is acquired using cordova.getActivity() */
        AudioManager mAudioManager = (AudioManager) this.cordova.getActivity().getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);

        /* Check Record Audio Permissions */

        String error = isValidCall(action, args);
        try {
            if (error.isEmpty()) {
                switch (Actions.fromString(action)) {
                    case MUTE:
                        mAudioManager.setStreamVolume(args.getInt(0), 0, AudioManager.FLAG_SHOW_UI);
                        callbackContext.success();
                        return true;
                    case SETVOLUME:
                        mAudioManager.setStreamVolume(args.getInt(0), args.getInt(1), AudioManager.FLAG_SHOW_UI);
                        callbackContext.success();
                        return true;
                    case GETVOLUME:
                        callbackContext.success(mAudioManager.getStreamVolume(args.getInt(0)));
                        return true;
                    case START_LISTENING:
                        tryUnregisterReceiver(jackIntentReceiver);

                        boolean triggerNot = args.getBoolean(0);
                        jackIntentReceiver = new JackIntentReceiver(callbackContext,
                                triggerNot ? preferences.getString(NOTIFICATION_TITLE, NOTIFICATION_TITLE_DEFVAL) : "",
                                triggerNot ? preferences.getString(NOTIFICATION_MESSAGE, NOTIFICATION_MESSAGE_DEFVAL) : "",
                                Notification.PRIORITY_MAX);
                        cordova.getActivity().registerReceiver(jackIntentReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
                        PluginResult pr = new PluginResult(PluginResult.Status.NO_RESULT);
                        pr.setKeepCallback(true);
                        callbackContext.sendPluginResult(pr);
                        return true;
                    case STOP_LISTENING:
                        tryUnregisterReceiver(jackIntentReceiver);
                        callbackContext.success();
                        jackIntentReceiver.cc.success("Receiver Cancelled - Closing CallbackContext ");
                        return true;
                }
                return true;
            } else {
                LOG.d(TAG, INVALID_ACTION_ERROR);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, error));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
        return false;
    }

    private boolean isXBetween(int x, int min, int max) {
        return x >= min && x <= max;
    }

    private String isValidCall(String action, JSONArray args) {
        boolean isValid;
        Actions a = Actions.fromString(action);
        String errorDesc = INVALID_ARGS_ERROR + a.argsDesc;
        switch (a) {
            case MUTE:
                int streamId = args.optInt(0, -2);
                isValid = args.length() == 1 && isXBetween(streamId, -1, 10);
                return isValid ? "" : errorDesc;
            case SETVOLUME:
                streamId = args.optInt(0, -2);
                isValid = args.length() == 2 && isXBetween(streamId, -1, 10) && args.optInt(1, -1) >= 0;
                return isValid ? "" : errorDesc;
            case GETVOLUME:
                streamId = args.optInt(0, -2);
                isValid = args.length() == 1 && isXBetween(streamId, -1, 10);
                return isValid ? "" : errorDesc;
            case STOP_LISTENING:
                return args.length() == 0 ? "" : errorDesc;
            case START_LISTENING:
                try {
                    args.getBoolean(0);
                    isValid = true;
                } catch (JSONException e) {
                    //silence error
                    isValid = false;
                }
                return isValid ? "" : errorDesc;
        }
        return INVALID_ACTION_ERROR;
    }

    void tryUnregisterReceiver(BroadcastReceiver receiver) {
        //check if there is a previous registered jackIntentReceiver and unregister
        try {
            if (receiver != null) {
                cordova.getActivity().unregisterReceiver(receiver);
            }
        } catch (IllegalArgumentException e) {
            //ignore
        }
    }

    enum Actions {
        MUTE("mute", "Invalid arguments-> StreamId: int"),
        SETVOLUME("setVolume", "StreamId: int, volume: int"),
        GETVOLUME("getVolume", "StreamId: int"),
        START_LISTENING("startListening", "triggerNotification: bool"),
        STOP_LISTENING("stopListening", "No arguments needed"),
        INVALID("", "Invalid action");

        String name;
        String argsDesc;

        Actions(String name, String argsDesc) {
            this.name = name;
            this.argsDesc = argsDesc;
        }

        public static Actions fromString(String action) {
            for (Actions a : Actions.values()) {
                if (a.name.equalsIgnoreCase(action)) {
                    return a;
                }
            }
            return INVALID;
        }
    }

    private class JackIntentReceiver extends BroadcastReceiver {
        private String title;
        private String text;
        private Integer priority;
        private CallbackContext cc;

        public JackIntentReceiver(CallbackContext cc, String title, String text, Integer priority) {
            this.title = title;
            this.text = text;
            this.priority = priority;
            this.cc = cc;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
                int state = intent.getIntExtra("state", -1);
                PluginResult pr;
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                switch (state) {
                    case 0:
                        Log.d(TAG, "Headset is unplugged");
                        if (!title.isEmpty()) {
                            notificationManager.notify(0, createNotification(title, text, priority));
                        }
                        pr = new PluginResult(PluginResult.Status.OK, "unplugged");
                        pr.setKeepCallback(true);
                        cc.sendPluginResult(pr);
                        break;
                    case 1:
                        Log.d(TAG, "Headset is plugged");
                        notificationManager.cancel(0);
                        pr = new PluginResult(PluginResult.Status.OK, "plugged");
                        pr.setKeepCallback(true);
                        cc.sendPluginResult(pr);
                        break;
                    default:
                        Log.d(TAG, "I have no idea what the headset state is");
                }
            }
        }

    }

    private static int getImageId(Resources resources, String icon, String packageName) {
        int iconId = resources.getIdentifier(icon, "drawable", packageName);
        if (iconId == 0) { //if icon not found, use default image
            iconId = android.R.drawable.ic_lock_silent_mode;
        }
        return iconId;
    }
}
