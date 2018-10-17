package it.near.sdk.jsonapiparser;

import com.google.common.collect.Maps;

import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Federico Boschini
 */
public class ErrorTest {

    // ERROR

    @Test
    public void idWorksProperly() {
        Error error = new Error();

        String id = "iddi";
        error.setId(id);

        assertThat(error.getId(), is(id));
    }

    @Test
    public void statusWorksProperly() {
        Error error = new Error();

        String status = "status";
        error.setStatus(status);

        assertThat(error.getStatus(), is(status));
    }

    @Test
    public void codeWorksProperly() {
        Error error = new Error();

        String code = "code";
        error.setCode(code);

        assertThat(error.getCode(), is(code));
    }

    @Test
    public void titleWorksProperly() {
        Error error = new Error();

        String title = "title";
        error.setTitle(title);

        assertThat(error.getTitle(), is(title));
    }

    @Test
    public void detailWorksProperly() {
        Error error = new Error();

        String detail = "detail";
        error.setDetail(detail);

        assertThat(error.getDetail(), is(detail));
    }

    @Test
    public void sourceWorksProperly() {
        Error error = new Error();

        Source source = new Source();
        error.setSource(source);

        assertThat(error.getSource(), is(source));
    }

    @Test
    public void linksWorkProperly() {
        Error error = new Error();

        ErrorLinks errorLinks = new ErrorLinks();
        error.setLinks(errorLinks);

        assertThat(error.getLinks(), is(errorLinks));
    }

    @Test
    public void metaWorkProperly() {
        Error error = new Error();

        HashMap<String, Object> meta = Maps.newHashMap();
        error.setMeta(meta);

        assertThat(error.getMeta(), is(meta));
    }


    //  SOURCE

    @Test
    public void parameterWorksProperly() {
        Source source = new Source();

        String parameter = "param";
        source.setParameter(parameter);

        assertThat(source.getParameter(), is(parameter));
    }

    @Test
    public void pointerWorksProperly() {
        Source source = new Source();

        String pointer = "pointer";
        source.setPointer(pointer);

        assertThat(source.getPointer(), is(pointer));
    }


    //  ERRORLINKS

    @Test
    public void aboutWorksProperly() {
        ErrorLinks errorLinks = new ErrorLinks();

        String about = "about";
        errorLinks.setAbout(about);

        assertThat(errorLinks.getAbout(), is(about));
    }
}