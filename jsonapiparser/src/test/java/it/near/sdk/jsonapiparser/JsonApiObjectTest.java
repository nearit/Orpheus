package it.near.sdk.jsonapiparser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Federico Boschini
 */
public class JsonApiObjectTest {

    @Test
    public void resourceWorksProperly() {
        JsonApiObject jsonApiObject = new JsonApiObject();

        Resource res = new Resource();
        jsonApiObject.setResource(res);

        assertThat(jsonApiObject.getResource(), is(instanceOf(Resource.class)));
        Resource r = (Resource) jsonApiObject.getResource();
        assertThat(r, equalTo(res));
    }

    @Test
    public void resourcesWorkProperly() {
        JsonApiObject jsonApiObject = new JsonApiObject();

        Resource res = new Resource();
        Resource res2 = new Resource();
        List<Resource> resources = Lists.newArrayList();
        resources.add(res);
        resources.add(res2);
        jsonApiObject.setResources(resources);

        List<Resource> actualResources = jsonApiObject.getResources();
        assertThat(actualResources.size(), is(2));
        assertThat(actualResources, is(resources));
        assertThat(actualResources.get(0), is(res));
        assertThat(actualResources.get(1), is(res2));
    }

    @Test
    public void includedWorkProperly() {
        JsonApiObject jsonApiObject = new JsonApiObject();

        Resource res = new Resource();
        Resource res2 = new Resource();
        List<Resource> included = Lists.newArrayList();
        included.add(res);
        included.add(res2);
        jsonApiObject.setIncluded(included);

        List<Resource> actualIncluded = jsonApiObject.getIncluded();
        assertThat(actualIncluded.size(), is(2));
        assertThat(actualIncluded, is(included));
        assertThat(actualIncluded.get(0), is(res));
        assertThat(actualIncluded.get(1), is(res2));
    }

    @Test
    public void metaWorksProperly() {
        JsonApiObject jsonApiObject = new JsonApiObject();

        HashMap<String, Object> meta = Maps.newHashMap();
        jsonApiObject.setMeta(meta);

        assertThat(jsonApiObject.getMeta(), is(meta));
    }

    @Test
    public void errorsWorkProperly() {
        JsonApiObject jsonApiObject = new JsonApiObject();

        Error error = new Error();
        List<Error> errors = Lists.newArrayList();
        errors.add(error);
        jsonApiObject.setErrors(errors);

        List<Error> actualErrors = jsonApiObject.getErrors();
        assertThat(errors, is(errors));
        assertThat(actualErrors.get(0), is(error));
    }

    @Test
    public void linksWorkProperly() {
        JsonApiObject jsonApiObject = new JsonApiObject();

        Links links = new Links();
        jsonApiObject.setLinks(links);

        assertThat(jsonApiObject.getLinks(), is(links));
    }

}