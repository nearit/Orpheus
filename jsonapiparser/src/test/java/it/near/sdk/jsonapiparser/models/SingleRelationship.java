package it.near.sdk.jsonapiparser.models;

import com.google.gson.annotations.SerializedName;

import it.near.sdk.jsonapiparser.Resource;
import it.near.sdk.jsonapiparser.annotations.Relationship;

public class SingleRelationship extends Resource {

    @SerializedName("content")
    public String content;
    @Relationship("child")
    public SingleRelationship child;

    public SingleRelationship() {
    }
}
