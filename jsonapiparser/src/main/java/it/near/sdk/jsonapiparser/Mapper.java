package it.near.sdk.jsonapiparser;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import it.near.sdk.jsonapiparser.annotations.Relationship;
import it.near.sdk.jsonapiparser.exceptions.NotExtendingResourceException;


/**
 * Mapper will map all different top-level members and will
 * also map the relations.
 * <p>
 * Includes will also mapped to matching relationship members.
 */
public class Mapper {

    private Deserializer mDeserializer;
    private AttributeMapper mAttributeMapper;
    private Factory factory;

    public Mapper() {
        mDeserializer = new Deserializer();
        mAttributeMapper = new AttributeMapper();
    }

    public Mapper(Deserializer deserializer, AttributeMapper attributeMapper) {
        mDeserializer = deserializer;
        mAttributeMapper = attributeMapper;
    }

    //TODO map href and meta (http://jsonapi.org/format/#document-links)

    /**
     * Will map links and return them.
     *
     * @param linksJsonObject JSONObject from link.
     * @return Links with mapped values.
     */
    public Links mapLinks(JSONObject linksJsonObject) {
        Links links = new Links();
        try {
            links.setSelfLink(linksJsonObject.getString("self"));
        } catch (JSONException e) {
            Logger.debug("JSON link does not contain self");
        }

        try {
            links.setRelated(linksJsonObject.getString("related"));
        } catch (JSONException e) {
            Logger.debug("JSON link does not contain related");
        }

        try {
            links.setFirst(linksJsonObject.getString("first"));
        } catch (JSONException e) {
            Logger.debug("JSON link does not contain first");
        }

        try {
            links.setLast(linksJsonObject.getString("last"));
        } catch (JSONException e) {
            Logger.debug("JSON link does not contain last");
        }

        try {
            links.setPrev(linksJsonObject.getString("prev"));
        } catch (JSONException e) {
            Logger.debug("JSON link does not contain prev");
        }

        try {
            links.setNext(linksJsonObject.getString("next"));
        } catch (JSONException e) {
            Logger.debug("JSON link does not contain next");
        }

        return links;
    }

    /**
     * Map the Id from json to the object.
     *
     * @param object         Object of the class.
     * @param jsonDataObject JSONObject of the dataNode.
     * @return Object with mapped fields.
     * @throws NotExtendingResourceException Throws when the object is not extending {@link Resource}
     */
    public Resource mapId(Resource object, JSONObject jsonDataObject) throws NotExtendingResourceException {
        try {
            return mDeserializer.setIdField(object, jsonDataObject.get("id"));
        } catch (JSONException e) {
            Logger.debug("JSON data does not contain id.");
        }

        return object;
    }

    /**
     * Maps the attributes of json to the object.
     *
     * @param object               Object of the class.
     * @param attributesJsonObject Attributes object inside the data node.
     * @return Object with mapped fields.
     */
    public Resource mapAttributes(Resource object, JSONObject attributesJsonObject) throws IncompatibleClassChangeError {
        if (attributesJsonObject == null) {
            return object;
        }

        for (Field field : getAllFields(object.getClass())) {
            // get the right attribute name
            String jsonFieldName = field.getName();
            boolean isRelation = false;
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation.annotationType() == SerializedName.class) {
                    SerializedName serializeName = (SerializedName) annotation;
                    jsonFieldName = serializeName.value();
                }
                if (annotation.annotationType() == Relationship.class) {
                    isRelation = true;
                }
            }

            if (isRelation) {
                continue;
            }
            if (field.isSynthetic()) {
                continue;
            }
            mAttributeMapper.mapAttributeToObject(object, attributesJsonObject, field, jsonFieldName);
        }

        return object;
    }

    public void mapRelations(HashMap<String, Resource> resources) {
        String DATA = "data";
        String ID = "id";
        for (Resource resource : resources.values()) {
            HashMap<String, String> relationshipNames = getRelationshipNames(resource.getClass());

            JSONObject resourceRelationships = null;
            try {
                resourceRelationships = resource.getJsonSourceObject().getJSONObject("relationships");
            } catch (JSONException e) {
                Logger.debug("Resource does not have relationships");
            }

            if (resourceRelationships != null && !relationshipNames.isEmpty()) {
                for (String relationshipKey : relationshipNames.keySet()) {
                    JSONObject relation = null;
                    try {
                        relation = resourceRelationships.getJSONObject(relationshipKey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (relation != null) {
                        try {
                            // DATA is object?
                            String relationId = relation.getJSONObject(DATA).getString(ID);
                            mDeserializer.setField(resource, relationshipNames.get(relationshipKey), resources.get(relationId));
                        } catch (JSONException e) {

                        }
                        try {
                            JSONArray relations = relation.getJSONArray(DATA);
                            List<Resource> rela = new ArrayList<>();
                            for (int i = 0; i< relations.length(); i++) {
                                String relationId = relations.getJSONObject(i).getString(ID);
                                rela.add(resources.get(relationId));
                            }

                            mDeserializer.setField(resource, relationshipNames.get(relationshipKey), rela);
                        } catch (JSONException e) {

                        }
                    }
                }
            }
        }
    }


    public List<Error> mapErrors(JSONArray errorArray) {
        List<Error> errors = new ArrayList<>();

        for (int i = 0; errorArray.length() > i; i++) {
            JSONObject errorJsonObject;
            try {
                errorJsonObject = errorArray.getJSONObject(i);
            } catch (JSONException e) {
                Logger.debug("No index " + i + " in error json array");
                continue;
            }
            Error error = new Error();

            try {
                error.setId(errorJsonObject.getString("id"));
            } catch (JSONException e) {
                Logger.debug("JSON object does not contain id");
            }

            try {
                error.setStatus(errorJsonObject.getString("status"));
            } catch (JSONException e) {
                Logger.debug("JSON object does not contain status");
            }

            try {
                error.setCode(errorJsonObject.getString("code"));
            } catch (JSONException e) {
                Logger.debug("JSON object does not contain code");
            }

            try {
                error.setTitle(errorJsonObject.getString("title"));
            } catch (JSONException e) {
                Logger.debug("JSON object does not contain title");
            }

            try {
                error.setDetail(errorJsonObject.getString("detail"));
            } catch (JSONException e) {
                Logger.debug("JSON object does not contain detail");
            }

            JSONObject sourceJsonObject = null;
            try {
                sourceJsonObject = errorJsonObject.getJSONObject("source");
            } catch (JSONException e) {
                Logger.debug("JSON object does not contain source");
            }

            if (sourceJsonObject != null) {
                Source source = new Source();
                try {
                    source.setParameter(sourceJsonObject.getString("parameter"));
                } catch (JSONException e) {
                    Logger.debug("JSON object does not contain parameter");
                }
                try {
                    source.setPointer(sourceJsonObject.getString("pointer"));
                } catch (JSONException e) {
                    Logger.debug("JSON object does not contain pointer");
                }
                error.setSource(source);
            }

            try {
                JSONObject linksJsonObject = errorJsonObject.getJSONObject("links");
                ErrorLinks links = new ErrorLinks();
                links.setAbout(linksJsonObject.getString("about"));
                error.setLinks(links);
            } catch (JSONException e) {
                Logger.debug("JSON object does not contain links or about");
            }

            try {
                error.setMeta(mAttributeMapper.createMapFromJSONObject(errorJsonObject.getJSONObject("meta")));
            } catch (JSONException e) {
                Logger.debug("JSON object does not contain JSONObject meta");
            }

            errors.add(error);
        }

        return errors;
    }

    //helper

    /**
     * Get the annotated relationship names.
     *
     * @param clazz Class for annotation.
     * @return List of relationship names.
     */
    private HashMap<String, String> getRelationshipNames(Class clazz) throws IncompatibleClassChangeError {
        HashMap<String, String> relationNames = new HashMap<>();
        for (Field field : getAllFields(clazz)) {
            String fieldName = field.getName();
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType() == SerializedName.class) {
                    SerializedName serializeName = (SerializedName) annotation;
                    fieldName = serializeName.value();
                }
                if (annotation.annotationType() == Relationship.class) {
                    Relationship relationshipAnnotation = (Relationship) annotation;
                    relationNames.put(relationshipAnnotation.value(), fieldName);
                }
            }
        }

        return relationNames;
    }

    private List<Field> getAllFields(Class clazz) {
        return getAllFieldsRec(clazz, new ArrayList<Field>());
    }

    private List<Field> getAllFieldsRec(Class clazz, List<Field> list) {
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            getAllFieldsRec(superClazz, list);
        }
        list.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return list;
    }

    // getter

    public Deserializer getDeserializer() {
        return mDeserializer;
    }

    public AttributeMapper getAttributeMapper() {
        return mAttributeMapper;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

}
