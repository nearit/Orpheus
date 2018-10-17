package it.near.sdk.jsonapiparser;

import android.util.Log;


/**
 * Logger you can turn on and off.
 */
public class Logger {
    private static final String TAG = "JsonAPIParser";
    private static boolean debug = false;
    private static boolean error = false;

    public static void debug(String message) {
        if (debug) {
            Log.d(TAG, message);
        }
    }

    public static void debug(String tag, String message) {
        if (debug) {
            Log.d(tag, message);
        }
    }

    public static void error(String message) {
        if (error) {
            Log.e(TAG, message);
        }
    }

    public static void error(String tag, String message) {
        if (error) {
            Log.e(tag, message);
        }
    }

    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    public static void setError(boolean error) {
        Logger.error = error;
    }
}
