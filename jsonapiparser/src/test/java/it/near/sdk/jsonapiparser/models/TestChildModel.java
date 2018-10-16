package it.near.sdk.jsonapiparser.models;

import com.google.gson.annotations.SerializedName;

import it.near.sdk.jsonapiparser.Resource;

/**
 * Created by cattaneostefano on 06/03/2017.
 */

public class TestChildModel extends Resource {
    @SerializedName("favourite_child")
    public boolean isFavoChild;

    public TestChildModel() {
    }

    public boolean getIsFavoChild() {
        return isFavoChild;
    }
}
