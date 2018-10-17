package it.near.sdk.jsonapiparser;

import com.google.common.collect.Maps;

import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Federico Boschini
 */
public class ResourceTest {

    @Test
    public void metaWorkProperly() {
        Resource res = new Resource();

        HashMap<String, Object> meta = Maps.newHashMap();
        res.setMeta(meta);

        assertThat(res.getMeta(), is(meta));
    }

    @Test
    public void linksWorkProperly() {
        Resource res = new Resource();

        Links links = new Links();
        res.setLinks(links);

        assertThat(res.getLinks(), is(links));
    }

    @Test
    public void jsonSourceObjectWorksProperly() {
        Resource res = new Resource();

        JSONObject jsonObject = new JSONObject();
        res.setJsonSourceObject(jsonObject);

        assertThat(res.getJsonSourceObject(), is(jsonObject));
    }

    @Test
    public void idWorksProperly() {
        Resource res = new Resource();

        String id = "iddi";
        res.setId(id);

        assertThat(res.getId(), is(id));
    }

}