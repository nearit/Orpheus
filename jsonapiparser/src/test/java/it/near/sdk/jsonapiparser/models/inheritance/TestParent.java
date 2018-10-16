package it.near.sdk.jsonapiparser.models.inheritance;

import com.google.gson.annotations.SerializedName;

import it.near.sdk.jsonapiparser.Resource;

public class TestParent extends Resource {

    @SerializedName("address")
    public String address;

    public TestParent() {
    }
}
