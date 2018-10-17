package it.near.sdk.performancetestapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import it.near.sdk.jsonapiparser.JsonAPIParser;
import it.near.sdk.jsonapiparser.utils.JsonAPIUtils;
import it.near.sdk.performancetestapplication.models.BeaconNode;
import it.near.sdk.performancetestapplication.models.GeofenceNode;
import it.near.sdk.performancetestapplication.models.Node;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_TAG = "FROM_FILE";
    private static final String SP_TAG = "FROM_SHAREDPREFS";

    private JsonAPIParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        parser = new JsonAPIParser();
        parser.getFactory().getDeserializer().registerResourceClass("nodes", Node.class);
        parser.getFactory().getDeserializer().registerResourceClass("beacon_nodes", BeaconNode.class);
        parser.getFactory().getDeserializer().registerResourceClass("geofence_nodes", GeofenceNode.class);

        loadNodes(parser);
        Log.e(FILE_TAG, "jsonApi parsed");
    }

    private List<Node> loadNodes(JsonAPIParser parser) {
        Log.e(FILE_TAG, "starting loading from assets");
        String config = null;
        try {
            config = getStringFromFile();
            Log.e(FILE_TAG, "loaded from file");
        } catch (Exception e) {
            Log.e(FILE_TAG, "cant get file content");
        }


        if (config == null) return null;
        JSONObject configJson;
        Log.e(FILE_TAG, "starting string -> jsonObject conversion");
        try {
            configJson = new JSONObject(config);
            Log.e(FILE_TAG, "jsonObject conversion done");
            saveToSharedPrefs(configJson);
        } catch (JSONException e) {
            return null;
        }
        Log.e(FILE_TAG, "starting jsonApi parsing");
        return JsonAPIUtils.parseList(parser, configJson, Node.class);
    }

    public String getStringFromFile() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("big_file_to_parse.json")));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public void loadFromSharedPrefs(View view) {
        SharedPreferences sp = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String cachedTree = sp.getString("tree", null);

        if (cachedTree == null) {
            Log.e(SP_TAG, "no cached tree in SP");
        } else {
            JSONObject configJson;
            try {
                configJson = new JSONObject(cachedTree);
                Log.e(SP_TAG, "jsonObject conversion done");
                Log.e(SP_TAG, "starting jsonApi parsing");
                JsonAPIUtils.parseList(parser, configJson, Node.class);
                Log.e(SP_TAG, "jsonApi parsed");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToSharedPrefs(JSONObject jsonObject) {
        SharedPreferences sp = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        sp.edit().putString("tree", jsonObject.toString()).apply();
    }
}
