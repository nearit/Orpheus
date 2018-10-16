package it.near.sdk.jsonapiparser.utils;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.near.sdk.jsonapiparser.JsonAPIParser;
import it.near.sdk.jsonapiparser.Logger;
import it.near.sdk.jsonapiparser.JsonApiObject;
import it.near.sdk.jsonapiparser.Resource;

import static it.near.sdk.jsonapiparser.utils.lang.Lang.safe;

/**
 * Serializing utils methods for building json api coded strings.
 */

public class JsonAPIUtils {
    private static final String TAG = "NearJsonAPIUtils";
    private static final String KEY_DATA_ELEMENT = "data";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ATTRIBUTES = "attributes";

    /**
     * Turns an hashmap of values to a jsonapi resource string.
     *
     * @param type the type of the jsonapi resource.
     * @param map  the attribute map.
     * @return codified string.
     * @throws JSONException if map can be transformed into JSONObject
     */
    public static String toJsonAPI(String type, HashMap<String, Object> map) throws JSONException {
        return toJsonAPI(type, null, map);
    }

    /**
     * Turns a list of hashmaps into a json api array of resources of the same type.
     *
     * @param type the type of the jsonapi resource.
     * @param maps the maps of attributes.
     * @return codified string.
     * @throws JSONException if map can be transformed into JSONObject
     */
    public static String toJsonAPI(String type, List<HashMap<String, Object>> maps) throws JSONException {
        JSONArray resources = new JSONArray();
        for (HashMap<String, Object> map : safe(maps)) {
            resources.put(buildResourceObject(type, null, map));
        }
        JSONObject jsonApiObject = new JSONObject();
        jsonApiObject.put(KEY_DATA_ELEMENT, resources);
        return jsonApiObject.toString();
    }

    /**
     * Turns a list of hashmaps into a json api array of resources of the same type and with given ids.
     *
     * @param type the type of the jsonapi resource.
     * @param pairs the pairs of id and type.
     * @return codified string
     * @throws JSONException if object can't be constructed
     */
    public static String toJsonAPIWithIds(String type,
                                          List<Pair<String, HashMap<String, Object>>> pairs) throws JSONException, IllegalArgumentException {
        if (pairs == null) {
            throw new IllegalArgumentException("Pairs of <type, id> must be non null");
        }
        JSONArray resources = new JSONArray();
        for (Pair<String, HashMap<String, Object>> pair : pairs) {
            resources.put(buildResourceObject(type, pair.first, pair.second));
        }
        JSONObject jsonApiObject = new JSONObject();
        jsonApiObject.put(KEY_DATA_ELEMENT, resources);
        return jsonApiObject.toString();
    }

    /**
     * Turns an hashmap of values to a jsonapi resource string. Also sets the id.
     *
     * @param type jsonapi resource type.
     * @param id   id of the resource.
     * @param map  values map.
     * @return codified string.
     * @throws JSONException if map can be transformed into JSONObject
     */
    public static String toJsonAPI(String type, String id, HashMap<String, Object> map) throws JSONException {
        JSONObject resource = buildResourceObject(type, id, map);
        JSONObject jsonApiObject = new JSONObject();
        jsonApiObject.put(KEY_DATA_ELEMENT, resource);
        return jsonApiObject.toString();
    }

    public static String toJsonAPINoAttributes(String type, List<? extends Resource> resources) throws JSONException, IllegalArgumentException {
        if (resources == null) {
            throw new IllegalArgumentException("Missing resources");
        }
        JSONArray jsonResources = new JSONArray();
        for (Resource resource : resources) {
            jsonResources.put(buildResourceObject(type, resource));
        }
        JSONObject jsonApiObject = new JSONObject();
        jsonApiObject.put(KEY_DATA_ELEMENT, jsonResources);
        return jsonApiObject.toString();
    }

    private static JSONObject buildResourceObject(String type, Resource resource) throws JSONException, IllegalArgumentException {
        if (resource == null) {
            throw new IllegalArgumentException("Missing Resource");
        }
        if (type == null) {
            throw new IllegalArgumentException("Missing resource type");
        }
        if (resource.getId() == null) {
            throw new IllegalArgumentException("Missing resource id");
        }

        JSONObject jsonResource = new JSONObject();
        jsonResource.put(KEY_ID, resource.getId());
        jsonResource.put(KEY_TYPE, type);
        jsonResource.put(KEY_ATTRIBUTES, new JSONObject());
        return jsonResource;
    }

    /**
     * Build the data object of the jsonapi resource.
     *
     * @param type jsonapi resource type.
     * @param id   id of the resource.
     * @param map  values map
     * @return JSONObject representation of map object.
     * @throws JSONException if map can be transformed into JSONObject
     */
    private static JSONObject buildResourceObject(String type, String id, HashMap<String, Object> map) throws JSONException {
        if (type == null) {
            throw new IllegalArgumentException("Missing resource type");
        }
        if (map == null)
            throw new IllegalArgumentException("Attribute map can't be null");

        JSONObject attributes = new JSONObject();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof HashMap) {
                attributes.put(entry.getKey(), new JSONObject((Map) entry.getValue()));
            } else {
                attributes.put(entry.getKey(), entry.getValue() != null ? entry.getValue() : JSONObject.NULL);
            }
        }

        JSONObject resource = new JSONObject();
        if (id != null) {
            resource.put(KEY_ID, id);
        }
        resource.put(KEY_TYPE, type);
        resource.put(KEY_ATTRIBUTES, attributes);
        return resource;
    }

    /**
     * Parse a list.
     *
     * @param jsonAPIParser the jsonAPIParser object. Its serializer must have been set to decode the Class of the objects of the list.
     * @param json     json to parse.
     * @param clazz    class of the list object.
     * @param <T>      generic type.
     * @return list of objects.
     */
    public static <T> List<T> parseList(JsonAPIParser jsonAPIParser, JSONObject json, Class<T> clazz) {
        return parseListAndMeta(jsonAPIParser, json, clazz).list;
    }

    public static <T> ListMetaBundle<T> parseListAndMeta(JsonAPIParser jsonAPIParser, JSONObject json, Class<T> clazz) {
        JsonApiObject jsonApiObject = null;
        try {
            jsonApiObject = jsonAPIParser.parse(json);
        } catch (Exception e) {
            Logger.debug("Parsing error");
        }

        ListMetaBundle<T> listMetaBundle = new ListMetaBundle<>();
        List<T> returnList = new ArrayList<T>();
        Map<String, Object> meta = new HashMap<>();
        if (jsonApiObject == null) {
            listMetaBundle.list = returnList;
            listMetaBundle.meta = meta;
            return listMetaBundle;
        } else {
            listMetaBundle.meta = jsonApiObject.getMeta();
            if (jsonApiObject.getResources() != null) {
                for (Resource r : jsonApiObject.getResources()) {
                    if (clazz.isInstance(r)) {
                        returnList.add((T) r);
                    }
                }
            }
            listMetaBundle.list = returnList;
            return listMetaBundle;
        }
    }

    /**
     * Parse an object.
     *
     * @param jsonAPIParser the jsonAPIParser object. Its serializer must have been set to decode the Class of the objects of the list.
     * @param json     json to parse.
     * @param clazz    class of the object.
     * @param <T>      generic type.
     * @return casted object.
     */
    public static <T> T parseElement(JsonAPIParser jsonAPIParser, JSONObject json, Class<T> clazz) {
        JsonApiObject jsonApiObject = null;
        try {
            jsonApiObject = jsonAPIParser.parse(json);
        } catch (Exception e) {
            Logger.debug("Parsing error");
        }
        return (T) jsonApiObject.getResource();
    }
}
