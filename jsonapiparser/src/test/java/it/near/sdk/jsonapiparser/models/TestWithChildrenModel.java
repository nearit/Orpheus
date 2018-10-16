package it.near.sdk.jsonapiparser.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import it.near.sdk.jsonapiparser.Resource;
import it.near.sdk.jsonapiparser.annotations.Relationship;


public class TestWithChildrenModel extends Resource {

    @SerializedName("content")
    public String content;
    @Relationship("children")
    public List<TestChildModel> children;

    public TestWithChildrenModel() {
    }
}
