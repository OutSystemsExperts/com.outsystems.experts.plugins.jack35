package com.outsystems.experts.plugins.jack35;

import android.content.Context;
import android.media.AudioManager;

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
    final String INVALID_ARGS_ERROR = "Invalid arguments";
    final String INVALID_ACTION_ERROR = "Invalid action";
    private AudioManager mAudioManager;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext)
            throws JSONException {
        /* Context is acquired using cordova.getActivity() */
        mAudioManager = (AudioManager) this.cordova.getActivity().getApplicationContext()
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
                }
                return true;
            } else {
                LOG.d(TAG, "invalid action or arguments");
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
        switch (a) {
            case MUTE:
                int streamId = args.optInt(0, -2);
                isValid = args.length() == 1 && isXBetween(streamId, -1, 10);
                return isValid ? "" : a.argsDesc;
            case SETVOLUME:
                streamId = args.optInt(0, -2);
                isValid = args.length() == 2 && isXBetween(streamId, -1, 10) && args.optInt(1, -1) >= 0;
                return isValid ? "" : a.argsDesc;
            case GETVOLUME:
                streamId = args.optInt(0, -2);
                isValid = args.length() == 1 && isXBetween(streamId, -1, 10);
                return isValid ? "" : a.argsDesc;
        }
        return "Invalid action";
    }

    private void mute(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    enum Actions {
        MUTE("mute", "Invalid arguments-> StreamId: int"),
        SETVOLUME("setVolume", "Invalid arguments-> StreamId: int, volume: int"),
        GETVOLUME("getVolume", "Invalid arguments-> StreamId: int"),
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
}
