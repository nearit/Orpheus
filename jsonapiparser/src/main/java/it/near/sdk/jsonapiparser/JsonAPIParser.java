package it.near.sdk.jsonapiparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * JsonAPIParser is a library to map JSON with the json:api specification format.
 * (http://jsonapi.org/).
 * <p>
 * Feel free to contribute on github. (//TODO insert new link here)
 * <p>
 * Example
 * <pre>
 * {@code
 *  JsonAPIParser jsonApiParser = new JsonAPIParser();
 *  JsonApiObject jsonapiObject = jsonApiParser.parse(YOUR-JSON-STRING);
 * }
 * </pre>
 */
public class JsonAPIParser {
    private Mapper mapper;
    private Factory factory;

    public JsonAPIParser() {
        mapper = new Mapper();
        factory = new Factory();
        factory.setMapper(mapper);
    }

    public JsonAPIParser(AttributeMapper attributeMapper) {
        mapper = new Mapper(new Deserializer(), attributeMapper);
        factory = new Factory();
        factory.setMapper(mapper);
    }

    public Factory getFactory() {
        return factory;
    }

    /**
     * Will return you an {@link JsonApiObject} with parsed objects, links, relations and includes.
     *
     * @param jsonString Your json:api formated string.
     * @return A {@link JsonApiObject}.
     * @throws JSONException or NotExtendingResourceException
     */
    public JsonApiObject parse(String jsonString) throws Exception {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw e;
        }

        return parseFromJSONObject(jsonObject);
    }

    public JsonApiObject parse(JSONObject jsonObject) throws Exception {
        return parseFromJSONObject(jsonObject);
    }

    /**
     * Parse and map all the top level members.
     */
    private JsonApiObject parseFromJSONObject(JSONObject jsonObject) throws Exception {
        JsonApiObject jsonApiObject = new JsonApiObject();

        //included
        try {
            JSONArray includedArray = jsonObject.getJSONArray("included");
            jsonApiObject.setIncluded(factory.newObjectFromJSONArray(includedArray));
        } catch (JSONException e) {
            Logger.debug("JSON does not contain included");
        }

        List<Resource> resourcesToLink = new ArrayList<>();

        //data array
        JSONArray dataArray = null;
        try {
            dataArray = jsonObject.getJSONArray("data");
            jsonApiObject.setResources(factory.newObjectFromJSONArray(dataArray));
            resourcesToLink.addAll(jsonApiObject.getResources());

        } catch (JSONException e) {
            Logger.debug("JSON does not contain data array");
        }

        //data object
        JSONObject dataObject = null;
        try {
            dataObject = jsonObject.getJSONObject("data");
            jsonApiObject.setResource(factory.newObjectFromJSONObject(dataObject));
            resourcesToLink.add((Resource) jsonApiObject.getResource());
        } catch (JSONException e) {
            Logger.debug("JSON does not contain data object");
        }

        //map relationships
        try {
            if (jsonApiObject.getIncluded() != null) {
                resourcesToLink.addAll(jsonApiObject.getIncluded());
            }
            HashMap<String, Resource> map = new HashMap<>();
            for (Resource resource : resourcesToLink) {
                map.put(resource.getId(), resource);
            }
            mapper.mapRelations(map);
        } catch (Exception e) {
            Logger.debug("unable to resolve relationships");
        } catch (IncompatibleClassChangeError samsung) {
            Logger.error("SAMSUNG: Error mapping relationships");
        }


        //link object
        JSONObject linkObject = null;
        try {
            linkObject = jsonObject.getJSONObject("links");
            jsonApiObject.setLinks(mapper.mapLinks(linkObject));
        } catch (JSONException e) {
            Logger.debug("JSON does not contain links object");
        }

        //meta object
        JSONObject metaObject = null;
        try {
            metaObject = jsonObject.getJSONObject("meta");
            jsonApiObject.setMeta(mapper.getAttributeMapper().createMapFromJSONObject(metaObject));
        } catch (JSONException e) {
            Logger.debug("JSON does not contain meta object");
        }

        JSONArray errorArray = null;
        try {
            errorArray = jsonObject.getJSONArray("errors");
            jsonApiObject.setErrors(mapper.mapErrors(errorArray));
        } catch (JSONException e) {
            Logger.debug("JSON does not contain errors object");
        }

        return jsonApiObject;
    }
}
