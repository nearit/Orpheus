package it.near.sdk.jsonapiparser;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * AttributeMapper is used to map the json:api attribute node to
 * your object fields.
 * <p>
 * You can create your own AttributeMapper and set it via {@link JsonAPIParser#JsonAPIParser(AttributeMapper)}.
 */
public class AttributeMapper {
    private Deserializer mDeserializer;

    public AttributeMapper() {
        mDeserializer = new Deserializer();
    }

    public AttributeMapper(Deserializer deserializer) {
        mDeserializer = deserializer;
    }

    /**
     * Will map the attributes of the JSONAPI attribute object.
     * JSONArrays will get mapped as {@literal List<Object>}.
     * JSONObject will get mapped as {@literal ArrayMap<String, Object>}.
     * Everything else will get mapped without changes.
     *
     * @param jsonApiResource      Object extended with {@link Resource} that will get the field set.
     * @param attributesJsonObject {@link JSONObject} with json:api attributes object
     * @param field                Field that will be set.
     * @param jsonFieldName        Name of the json-field in attributesJsonObject to get data from.
     */
    public void mapAttributeToObject(Resource jsonApiResource, JSONObject attributesJsonObject, Field field, String jsonFieldName) {
        try {
            if (attributesJsonObject.get(jsonFieldName).getClass() == JSONArray.class) {
                List<Object> list = createListFromJSONArray(attributesJsonObject.getJSONArray(jsonFieldName), field);
                mDeserializer.setField(jsonApiResource, field.getName(), list);
            } else if (attributesJsonObject.get(jsonFieldName).getClass() == JSONObject.class) {
                Gson gson = new Gson();
                Object obj = gson.fromJson(attributesJsonObject.get(jsonFieldName).toString(), field.getType());
                mDeserializer.setField(jsonApiResource, field.getName(), obj);
            } else {
                mDeserializer.setField(jsonApiResource, field.getName(), attributesJsonObject.get(jsonFieldName));
            }
        } catch (JSONException e) {
            Logger.debug("JSON attributes does not contain " + jsonFieldName);
        }
    }

    /**
     * Will loop through JSONArray and return values as List<Object>.
     *
     * @param jsonArray JSONArray with values.
     * @return List<Object> of JSONArray values.
     */
    private List<Object> createListFromJSONArray(JSONArray jsonArray, Field field) {
        Type genericFieldType = field.getGenericType();
        List<Object> objectArrayList = new ArrayList<>();

        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            for (Type fieldArgType : fieldArgTypes) {
                final Class fieldArgClass = (Class) fieldArgType;
                // System.out.println("fieldArgClass = " + fieldArgClass);

                for (int i = 0; jsonArray.length() > i; i++) {
                    Object obj = null;
                    try {
                        if (jsonArray.get(i) instanceof String) {
                            // if we are parsing an array of string we don't ask Gson to parse them
                            // because if they have spaces, it will crash the attribute mapping (even if an array of strings with spaces is a valid json)
                            obj = jsonArray.get(i);
                        } else {
                            obj = new Gson().fromJson(jsonArray.get(i).toString(), fieldArgClass);
                        }
                    } catch (JSONException e) {
                        Logger.debug("JSONArray does not contain index " + i + ".");
                    }
                    objectArrayList.add(obj);
                }
            }
        }

        return objectArrayList;
    }

    /**
     * Will loop through JSONObject and return values as arrayMap.
     *
     * @param jsonObject JSONObject for meta.
     * @return HashMap with meta values.
     */
    public HashMap<String, Object> createMapFromJSONObject(JSONObject jsonObject) {
        HashMap<String, Object> metaMap = new HashMap<>();

        for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
            String key = iter.next();

            try {
                metaMap.put(key, jsonObject.get(key));
            } catch (JSONException e) {
                Logger.debug("JSON does not contain " + key + ".");
            }
        }

        return metaMap;
    }

}
