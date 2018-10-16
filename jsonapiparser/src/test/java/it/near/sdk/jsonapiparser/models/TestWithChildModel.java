package it.near.sdk.jsonapiparser.models;

import com.google.gson.annotations.SerializedName;

import it.near.sdk.jsonapiparser.Resource;
import it.near.sdk.jsonapiparser.annotations.Relationship;

/**
 * Created by cattaneostefano on 06/03/2017.
 */

public class TestWithChildModel extends Resource {
    @SerializedName("content")
    public String content;
    @Relationship("child")
    public TestChildModel child;

    public TestWithChildModel() {
    }
}
