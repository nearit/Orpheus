package it.near.sdk.jsonapiparser.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.List;

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
        HashMap<String, Object> attributes = Maps.newHashMap();
        attributes.put("infinitooo", POSITIVE_INFINITY);
        String type = "tipo";
        JsonAPIUtils.toJsonAPI(type, attributes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiSingle_typeMUSTbeNotNull() throws JSONException {
        HashMap<String, Object> attributes = Maps.newHashMap();
        attributes.put("name", "bonetti");
        attributes.put("color", "black");
        JsonAPIUtils.toJsonAPI(null, attributes);
    }

    @Test
    public void toJsonApiSingleElement_returnsCorrectJsonAPI() throws JSONException {
        HashMap<String, Object> attributes = Maps.newHashMap();
        attributes.put("name", "bonetti");
        attributes.put("color", "black");
        String type = "goalkeeper";

        String jsonApiOutput = JsonAPIUtils.toJsonAPI(type, attributes);

        assertThat(jsonApiOutput, is(notNullValue()));
        JSONObject actualJson = new JSONObject(jsonApiOutput);
        assertThat(actualJson.has("data"), is(true));
        assertThat(actualJson.has("attributes"), is(false));
        actualJson = actualJson.getJSONObject("data");
        assertThat(actualJson, is(notNullValue()));
        assertThat(actualJson.getString("type"), is(type));
        assertThat(actualJson.has("attributes"), is(true));
        assertThat(actualJson.has("id"), is(false));
        actualJson = actualJson.getJSONObject("attributes");
        assertThat(actualJson, is(notNullValue()));
        assertThat(actualJson.getString("name"), is("bonetti"));
        assertThat(actualJson.getString("color"), is("black"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiWithId_ifResourcesAreNull_throw() throws JSONException {
        JsonAPIUtils.toJsonAPI("type", "id", null);
    }

    @Test(expected = JSONException.class)
    public void toJsonApiWithId_canThrowJSONException() throws JSONException {
        HashMap<String, Object> attributes = Maps.newHashMap();
        attributes.put("infinitooo", POSITIVE_INFINITY);
        String type = "tipo";
        String id = "id";

        JsonAPIUtils.toJsonAPI(type, id, attributes);
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

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiMultiElement_ifPairIsNull_throw() throws JSONException {
        JsonAPIUtils.toJsonAPIWithIds("type", null);
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
        JSONArray actualArray = actualJson.getJSONArray("data");
        assertThat(actualArray.length(), is(2));
        actualJson = actualArray.getJSONObject(0);
        assertThat(actualJson, is(notNullValue()));
        assertThat(actualJson.getString("type"), is(type));
        assertThat(actualJson.has("attributes"), is(true));
        actualJson = actualJson.getJSONObject("attributes");
        assertThat(actualJson.getString("name"), is("bonetti"));
        assertThat(actualJson.getString("color"), is("black"));
        actualJson = actualArray.getJSONObject(1);
        assertThat(actualJson.getString("type"), is(type));
        assertThat(actualJson.has("attributes"), is(true));
        actualJson = actualJson.getJSONObject("attributes");
        assertThat(actualJson.getString("name"), is("neuer"));
        assertThat(actualJson.getString("color"), is("white"));

    }


    // NO ATTRIBUTES

    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiNoAttributes_ifResourcesIsNull_throw() throws JSONException {
        JsonAPIUtils.toJsonAPINoAttributes("type", null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void toJsonApiWithId_ifTypeIsNull_throw() throws JSONException {
        HashMap<String, Object> attributes = Maps.newHashMap();
        attributes.put("name", "mario rossi");
        attributes.put("color", "black");
        JsonAPIUtils.toJsonAPI(null, "id", attributes);
    }

    @Test
    public void toJsonApiWithId() throws JSONException {
        HashMap<String, Object> attributes = Maps.newHashMap();
        attributes.put("name", "bonetti");
        attributes.put("color", "black");
        String type = "goalkeeper";
        String id = "1";
        String jsonApiOutput = JsonAPIUtils.toJsonAPI(type, id, attributes);
        assertThat(jsonApiOutput, is(notNullValue()));
        JSONObject actualJson = new JSONObject(jsonApiOutput);
        assertThat(actualJson.has("data"), is(true));
        assertThat(actualJson.has("attributes"), is(false));
        actualJson = actualJson.getJSONObject("data");
        assertThat(actualJson, is(notNullValue()));
        assertThat(actualJson.getString("type"), is(type));
        assertThat(actualJson.getString("id"), is("1"));
        assertThat(actualJson.has("attributes"), is(true));
        actualJson = actualJson.getJSONObject("attributes");
        assertThat(actualJson, is(notNullValue()));
        assertThat(actualJson.getString("name"), is("bonetti"));
        assertThat(actualJson.getString("color"), is("black"));
    }

    @Test
    public void toJsonApiFromIdTypePairs_() throws JSONException {

    }

}
