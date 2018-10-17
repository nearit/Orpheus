package it.near.sdk.jsonapiparser.utils;

import android.util.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.near.sdk.jsonapiparser.Links;
import it.near.sdk.jsonapiparser.Resource;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by cattaneostefano on 27/02/2017.
 */
@RunWith(JUnit4.class)
public class JsonAPIUtilsTest {

    //  SINGLE ELEMENT

    @Test(expected = JSONException.class)
    public void toJsonApiSingleElement_canThrowJSONException() throws JSONException {
        HashMap<String, Object> attributesMap = Maps.newHashMap();
        attributesMap.put("infinitooo", POSITIVE_INFINITY);
        String type = "tipo";
        JsonAPIUtils.toJsonAPI(type, attributesMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiSingle_typeMUSTbeNotNull() throws JSONException {
        HashMap<String, Object> attributesMap = Maps.newHashMap();
        attributesMap.put("name", "bonetti");
        attributesMap.put("color", "black");
        JsonAPIUtils.toJsonAPI(null, attributesMap);
    }

    @Test
    public void toJsonApiSingleElement_returnsCorrectJsonAPI() throws JSONException {
        HashMap<String, Object> attributesMap = Maps.newHashMap();
        attributesMap.put("name", "bonetti");
        attributesMap.put("color", "black");
        String type = "goalkeeper";

        String jsonApiOutput = JsonAPIUtils.toJsonAPI(type, attributesMap);

        assertThat(jsonApiOutput, is(notNullValue()));

        JSONObject actualJson = new JSONObject(jsonApiOutput);
        assertThat(actualJson.has("data"), is(true));
        assertThat(actualJson.has("attributes"), is(false));

        JSONObject data = actualJson.getJSONObject("data");
        assertThat(data, is(notNullValue()));
        assertThat(data.getString("type"), is(type));
        assertThat(data.has("attributes"), is(true));
        assertThat(data.has("id"), is(false));

        JSONObject attributes = data.getJSONObject("attributes");
        assertThat(attributes, is(notNullValue()));
        assertThat(attributes.getString("name"), is("bonetti"));
        assertThat(attributes.getString("color"), is("black"));
    }

    @Test
    public void toJsonApiSingleElement_withMapInMap_returnsCorrectJsonAPI() throws JSONException {
        HashMap<String, Object> attributesMap = Maps.newHashMap();
        attributesMap.put("name", "bonetti");
        HashMap<String, Object> nestedMap = Maps.newHashMap();
        nestedMap.put("color", "black");
        attributesMap.put("nested", nestedMap);
        String type = "goalkeeper";

        String jsonApiOutput = JsonAPIUtils.toJsonAPI(type, attributesMap);

        assertThat(jsonApiOutput, is(notNullValue()));

        JSONObject actualJson = new JSONObject(jsonApiOutput);
        assertThat(actualJson.has("data"), is(true));
        assertThat(actualJson.has("attributes"), is(false));

        JSONObject data = actualJson.getJSONObject("data");
        assertThat(data, is(notNullValue()));
        assertThat(data.getString("type"), is(type));
        assertThat(data.has("attributes"), is(true));
        assertThat(data.has("id"), is(false));

        JSONObject attributes = data.getJSONObject("attributes");
        assertThat(attributes, is(notNullValue()));
        assertThat(attributes.getString("name"), is("bonetti"));
        JSONObject nestedObject = attributes.getJSONObject("nested");
        assertThat(nestedObject, is(notNullValue()));
        assertThat(nestedObject.has("color"), is(true));
        assertThat(nestedObject.getString("color"), is("black"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiWithId_ifResourcesAreNull_throw() throws JSONException {
        JsonAPIUtils.toJsonAPI("type", "id", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiWithId_ifTypeIsNull_throw() throws JSONException {
        HashMap<String, Object> attributes = Maps.newHashMap();
        attributes.put("name", "mario rossi");
        attributes.put("color", "black");
        JsonAPIUtils.toJsonAPI(null, "id", attributes);
    }

    @Test(expected = JSONException.class)
    public void toJsonApiWithId_canThrowJSONException() throws JSONException {
        HashMap<String, Object> attributesMap = Maps.newHashMap();
        attributesMap.put("infinitooo", POSITIVE_INFINITY);
        String type = "tipo";
        String id = "id";

        JsonAPIUtils.toJsonAPI(type, id, attributesMap);
    }

    @Test
    public void toJsonApiWithId_returnsCorrectJsonAPI() throws JSONException {
        HashMap<String, Object> attributesMap = Maps.newHashMap();
        attributesMap.put("name", "mario");
        attributesMap.put("color", "rossi");
        String type = "typo";
        String id = "id";

        String output = JsonAPIUtils.toJsonAPI(type, id, attributesMap);

        assertThat(output, is(notNullValue()));

        JSONObject actualJson = new JSONObject(output);
        assertThat(actualJson.has("data"), is(true));
        assertThat(actualJson.has("attributes"), is(false));

        JSONObject data = actualJson.getJSONObject("data");
        assertThat(data, is(notNullValue()));
        assertThat(data.getString("type"), is(type));
        assertThat(data.has("attributes"), is(true));
        assertThat(data.has("id"), is(true));
        assertThat(data.getString("id"), is(id));

        JSONObject attributes = data.getJSONObject("attributes");
        assertThat(attributes, is(notNullValue()));
        assertThat(attributes.getString("name"), is("mario"));
        assertThat(attributes.getString("color"), is("rossi"));
    }

    @Test
    public void toJsonApiWithId_withMapInMap_returnsCorrectJsonAPI() throws JSONException {
        HashMap<String, Object> attributesMap = Maps.newHashMap();
        attributesMap.put("name", "mario");
        HashMap<String, Object> nestedMap = Maps.newHashMap();
        nestedMap.put("color", "rossi");
        attributesMap.put("nested", nestedMap);
        String type = "typo";
        String id = "id";

        String output = JsonAPIUtils.toJsonAPI(type, id, attributesMap);

        assertThat(output, is(notNullValue()));

        JSONObject actualJson = new JSONObject(output);
        assertThat(actualJson.has("data"), is(true));
        assertThat(actualJson.has("attributes"), is(false));

        JSONObject data = actualJson.getJSONObject("data");
        assertThat(data, is(notNullValue()));
        assertThat(data.getString("type"), is(type));
        assertThat(data.has("attributes"), is(true));
        assertThat(data.has("id"), is(true));
        assertThat(data.getString("id"), is(id));

        JSONObject attributes = data.getJSONObject("attributes");
        assertThat(attributes, is(notNullValue()));
        assertThat(attributes.getString("name"), is("mario"));
        JSONObject nestedObject = attributes.getJSONObject("nested");
        assertThat(nestedObject, is(notNullValue()));
        assertThat(nestedObject.has("color"), is(true));
        assertThat(nestedObject.getString("color"), is("rossi"));
    }


    // MULTI ELEMENT

    @Test(expected = JSONException.class)
    public void toJsonApiMultiElement_canThrowJSONException() throws JSONException {
        HashMap<String, Object> firstMap = Maps.newHashMap();
        firstMap.put("name", "bonetti");
        firstMap.put("color", "black");
        HashMap<String, Object> secondMap = Maps.newHashMap();
        secondMap.put("name", "neuer");
        secondMap.put("menooooo", NEGATIVE_INFINITY);
        List<HashMap<String, Object>> mapList = Lists.newArrayList();
        mapList.add(firstMap);
        mapList.add(secondMap);
        String type = "goalkeeper";

        JsonAPIUtils.toJsonAPI(type, mapList);
    }

    @Test
    public void toJsonApiMultiElement_returnsCorrectJsonAPI() throws JSONException {
        HashMap<String, Object> firstMap = Maps.newHashMap();
        firstMap.put("name", "bonetti");
        firstMap.put("color", "black");
        HashMap<String, Object> secondMap = Maps.newHashMap();
        secondMap.put("name", "neuer");
        secondMap.put("color", "white");
        List<HashMap<String, Object>> mapList = Lists.newArrayList();
        mapList.add(firstMap);
        mapList.add(secondMap);
        String type = "goalkeeper";

        String jsonApiOutput = JsonAPIUtils.toJsonAPI(type, mapList);

        assertThat(jsonApiOutput, is(notNullValue()));

        JSONObject actualJson = new JSONObject(jsonApiOutput);
        assertThat(actualJson.has("data"), is(true));

        JSONArray dataArray = actualJson.getJSONArray("data");
        assertThat(dataArray.length(), is(2));

        JSONObject first = dataArray.getJSONObject(0);
        assertThat(first, is(notNullValue()));
        assertThat(first.getString("type"), is(type));
        assertThat(first.has("attributes"), is(true));
        JSONObject firstAttributes = first.getJSONObject("attributes");
        assertThat(firstAttributes.getString("name"), is("bonetti"));
        assertThat(firstAttributes.getString("color"), is("black"));

        JSONObject second = dataArray.getJSONObject(1);
        assertThat(second.getString("type"), is(type));
        assertThat(second.has("attributes"), is(true));
        JSONObject secondAttributes = second.getJSONObject("attributes");
        assertThat(secondAttributes.getString("name"), is("neuer"));
        assertThat(secondAttributes.getString("color"), is("white"));

    }

    @Test
    public void toJsonApiMultiElement_withMapInMap_returnsCorrectJsonAPI() throws JSONException {
        HashMap<String, Object> firstMap = Maps.newHashMap();
        firstMap.put("name", "bonetti");
        firstMap.put("color", "black");
        HashMap<String, Object> secondMap = Maps.newHashMap();
        secondMap.put("name", "neuer");
        HashMap<String, Object> nestedMap = Maps.newHashMap();
        nestedMap.put("color", "white");
        secondMap.put("nested", nestedMap);
        List<HashMap<String, Object>> mapList = Lists.newArrayList();
        mapList.add(firstMap);
        mapList.add(secondMap);
        String type = "goalkeeper";

        String jsonApiOutput = JsonAPIUtils.toJsonAPI(type, mapList);

        assertThat(jsonApiOutput, is(notNullValue()));

        JSONObject actualJson = new JSONObject(jsonApiOutput);
        assertThat(actualJson.has("data"), is(true));

        JSONArray dataArray = actualJson.getJSONArray("data");
        assertThat(dataArray.length(), is(2));

        JSONObject first = dataArray.getJSONObject(0);
        assertThat(first, is(notNullValue()));
        assertThat(first.getString("type"), is(type));
        assertThat(first.has("attributes"), is(true));
        JSONObject firstAttributes = first.getJSONObject("attributes");
        assertThat(firstAttributes.getString("name"), is("bonetti"));
        assertThat(firstAttributes.getString("color"), is("black"));

        JSONObject second = dataArray.getJSONObject(1);
        assertThat(second.getString("type"), is(type));
        assertThat(second.has("attributes"), is(true));
        JSONObject secondAttributes = second.getJSONObject("attributes");
        assertThat(secondAttributes.getString("name"), is("neuer"));
        JSONObject nestedObject = secondAttributes.getJSONObject("nested");
        assertThat(nestedObject, is(notNullValue()));
        assertThat(nestedObject.has("color"), is(true));
        assertThat(nestedObject.getString("color"), is("white"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiMultiElementWithIds_ifPairIsNull_throw() throws JSONException {
        JsonAPIUtils.toJsonAPIWithIds("type", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiMultiElementWithIds_ifTypeIsNull_throw() throws JSONException {
        String id = "iddi";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("color", "black");
        Pair<String, HashMap<String, Object>> pair = Pair.create(id, map);
        List<Pair<String, HashMap<String, Object>>> pairs = Lists.newArrayList();
        pairs.add(pair);

        JsonAPIUtils.toJsonAPIWithIds(null, pairs);
    }

    @Test(expected = JSONException.class)
    public void toJsonAPIWithIds_canThrowJSONException() throws JSONException {
        String type = "typppo";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("color", POSITIVE_INFINITY);
        Pair<String, HashMap<String, Object>> pair = Pair.create("id", map);
        List<Pair<String, HashMap<String, Object>>> pairs = Lists.newArrayList();
        pairs.add(pair);

        JsonAPIUtils.toJsonAPIWithIds(type, pairs);
    }

    @Test
    public void toJsonAPIWithIds_returnsCorrectJsonAPI() throws JSONException {
        String type = "taip";
        HashMap<String, Object> map1 = Maps.newHashMap();
        map1.put("color", "black");
        HashMap<String, Object> map2 = Maps.newHashMap();
        map2.put("name", "mario");
        Pair<String, HashMap<String, Object>> pair1 = Pair.create("id1", map1);
        Pair<String, HashMap<String, Object>> pair2 = Pair.create("id2", map2);
        List<Pair<String, HashMap<String, Object>>> pairs = Lists.newArrayList();
        pairs.add(pair1);
        pairs.add(pair2);

        String jsonApiOutput = JsonAPIUtils.toJsonAPIWithIds(type, pairs);

        assertThat(jsonApiOutput, is(notNullValue()));

        JSONObject actualJson = new JSONObject(jsonApiOutput);
        assertThat(actualJson.has("data"), is(true));

        JSONArray dataArray = actualJson.getJSONArray("data");
        assertThat(dataArray.length(), is(2));

        JSONObject first = dataArray.getJSONObject(0);
        assertThat(first, is(notNullValue()));
        assertThat(first.getString("type"), is(type));
        assertThat(first.has("attributes"), is(true));
        JSONObject firstAttributes = first.getJSONObject("attributes");
        assertThat(firstAttributes.getString("color"), is("black"));

        JSONObject second = dataArray.getJSONObject(1);
        assertThat(second.getString("type"), is(type));
        assertThat(second.has("attributes"), is(true));
        JSONObject secondAttributes = second.getJSONObject("attributes");
        assertThat(secondAttributes.getString("name"), is("mario"));
    }

    @Test
    public void toJsonAPIWithIds_withMapInMap_returnsCorrectJsonAPI() throws JSONException {
        String type = "taip";
        HashMap<String, Object> map1 = Maps.newHashMap();
        map1.put("color", "black");
        HashMap<String, Object> map2 = Maps.newHashMap();
        map2.put("name", "mario");
        HashMap<String, Object> nestedMap = Maps.newHashMap();
        nestedMap.put("lastName", "rossi");
        map2.put("nested", nestedMap);
        Pair<String, HashMap<String, Object>> pair1 = Pair.create("id1", map1);
        Pair<String, HashMap<String, Object>> pair2 = Pair.create("id2", map2);
        List<Pair<String, HashMap<String, Object>>> pairs = Lists.newArrayList();
        pairs.add(pair1);
        pairs.add(pair2);

        String jsonApiOutput = JsonAPIUtils.toJsonAPIWithIds(type, pairs);

        assertThat(jsonApiOutput, is(notNullValue()));

        JSONObject actualJson = new JSONObject(jsonApiOutput);
        assertThat(actualJson.has("data"), is(true));

        JSONArray dataArray = actualJson.getJSONArray("data");
        assertThat(dataArray.length(), is(2));

        JSONObject first = dataArray.getJSONObject(0);
        assertThat(first, is(notNullValue()));
        assertThat(first.getString("type"), is(type));
        assertThat(first.has("attributes"), is(true));
        JSONObject firstAttributes = first.getJSONObject("attributes");
        assertThat(firstAttributes.getString("color"), is("black"));

        JSONObject second = dataArray.getJSONObject(1);
        assertThat(second.getString("type"), is(type));
        assertThat(second.has("attributes"), is(true));
        JSONObject secondAttributes = second.getJSONObject("attributes");
        assertThat(secondAttributes.getString("name"), is("mario"));
        JSONObject nestedObject = secondAttributes.getJSONObject("nested");
        assertThat(nestedObject, is(notNullValue()));
        assertThat(nestedObject.has("lastName"), is(true));
        assertThat(nestedObject.getString("lastName"), is("rossi"));
    }


    // NO ATTRIBUTES

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiNoAttributes_ifResourcesIsNull_throw() throws JSONException {
        JsonAPIUtils.toJsonAPINoAttributes("type", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiNoAttributes_ifTypeIsNull_throw() throws JSONException {
        List<Resource> list = new ArrayList<>();

        Resource resource = new Resource();
        resource.setId("resId");

        list.add(resource);

        JsonAPIUtils.toJsonAPINoAttributes(null, list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiNoAttributes_ifAResourceIsNull_throw() throws JSONException {
        List<Resource> list = new ArrayList<>();

        Resource resource = new Resource();
        resource.setId("resId");

        list.add(resource);
        list.add(null);

        JsonAPIUtils.toJsonAPINoAttributes("type", list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiNoAttributes_ifAResourceIdIsNull_throw() throws JSONException {
        List<Resource> list = new ArrayList<>();

        Resource resource = new Resource();
        resource.setId(null);

        list.add(resource);

        JsonAPIUtils.toJsonAPINoAttributes("type", list);
    }

    @Test
    public void toJsonApiNoAttributes_returnsCorrectJsonAPI() throws JSONException {
        String type = "tipo";
        String selfLink = "https://www.nearit.com";
        Links links = new Links();
        links.setSelfLink(selfLink);
        HashMap<String, Object> firstMap = Maps.newHashMap();
        firstMap.put("name", "bonetti");
        firstMap.put("color", "black");
        HashMap<String, Object> secondMap = Maps.newHashMap();
        secondMap.put("name", "neuer");
        secondMap.put("color", "white");

        List<Resource> list = new ArrayList<>();
        Resource resource = new Resource();
        resource.setId("resId");
        resource.setLinks(links);
        resource.setJsonSourceObject(new JSONObject(firstMap));
        Resource resource2 = new Resource();
        resource2.setId("res2Id");
        resource2.setLinks(links);
        resource2.setJsonSourceObject(new JSONObject(secondMap));
        list.add(resource);
        list.add(resource2);

        String output = JsonAPIUtils.toJsonAPINoAttributes(type, list);

        assertThat(output, is(notNullValue()));

        JSONObject actualJson = new JSONObject(output);
        assertThat(actualJson.has("data"), is(true));

        JSONArray dataArray = actualJson.getJSONArray("data");
        assertThat(dataArray.length(), is(2));

        JSONObject first = dataArray.getJSONObject(0);
        assertThat(first, is(notNullValue()));
        assertThat(first.getString("type"), is(type));
        assertThat(first.has("attributes"), is(true));

        JSONObject firstAttributes = first.getJSONObject("attributes");
        assertThat(firstAttributes.length(), is(0));

    }

}
