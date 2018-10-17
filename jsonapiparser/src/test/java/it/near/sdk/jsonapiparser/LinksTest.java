package it.near.sdk.jsonapiparser;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Federico Boschini
 */
public class LinksTest {

    @Test
    public void selfWorksProperly() {
        Links links = new Links();

        String self = "self";
        links.setSelfLink(self);

        assertThat(links.getSelfLink(), is(self));
    }

    @Test
    public void relatedWorksProperly() {
        Links links = new Links();

        String related = "related";
        links.setRelated(related);

        assertThat(links.getRelated(), is(related));
    }

    @Test
    public void firstWorksProperly() {
        Links links = new Links();

        String first = "first";
        links.setFirst(first);

        assertThat(links.getFirst(), is(first));
    }

    @Test
    public void lastWorksProperly() {
        Links links = new Links();

        String last = "last";
        links.setLast(last);

        assertThat(links.getLast(), is(last));
    }

    @Test
    public void prevWorksProperly() {
        Links links = new Links();

        String prev = "prev";
        links.setPrev(prev);

        assertThat(links.getPrev(), is(prev));
    }

    @Test
    public void nextWorksProperly() {
        Links links = new Links();

        String next = "next";
        links.setNext(next);

        assertThat(links.getNext(), is(next));
    }

    @Test
    public void aboutWorksProperly() {
        Links links = new Links();

        String about = "about";
        links.setAbout(about);

        assertThat(links.getAbout(), is(about));
    }

}