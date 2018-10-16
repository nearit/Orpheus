package it.near.sdk.jsonapiparser.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import it.near.sdk.jsonapiparser.Resource;

public class TestModel extends Resource {

    @SerializedName("content")
    public String content;
    @SerializedName("double_value")
    public Number double_value;
    @SerializedName("int_value")
    public Number int_value;
    @SerializedName("list_string")
    public List<String> strings;

    public TestModel() {
    }

}
