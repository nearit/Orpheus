package it.near.sdk.performancetestapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


import it.near.sdk.performancetestapplication.models.BeaconNode;
import it.near.sdk.performancetestapplication.models.GeofenceNode;
import it.near.sdk.performancetestapplication.models.Node;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Federico Boschini
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "FROM_BOOT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //loadFromCache(context);
        }
    }

    /*private void loadFromCache(Context context) {
        JsonAPIParser parser = new JsonAPIParser();
        parser.getFactory().getDeserializer().registerResourceClass("nodes", Node.class);
        parser.getFactory().getDeserializer().registerResourceClass("beacon_nodes", BeaconNode.class);
        parser.getFactory().getDeserializer().registerResourceClass("geofence_nodes", GeofenceNode.class);

        Log.e(TAG, "starting loading from cache");
        SharedPreferences sp = context.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String cachedTree = sp.getString("tree", null);
        Log.e(TAG, "loaded from cache");

        if (cachedTree == null) {
            Log.e(TAG, "no cached tree in SP");
        } else {
            JSONObject configJson;
            try {
                Log.e(TAG, "start string -> jsonObject conversion");
                configJson = new JSONObject(cachedTree);
                Log.e(TAG, "jsonObject conversion done");
                Log.e(TAG, "starting jsonApi parsing");
                JsonAPIUtils.parseList(parser, configJson, Node.class);
                Log.e(TAG, "jsonApi parsed");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
