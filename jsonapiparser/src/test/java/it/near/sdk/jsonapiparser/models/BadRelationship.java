package it.near.sdk.jsonapiparser.models;

import com.google.gson.annotations.SerializedName;

import it.near.sdk.jsonapiparser.Resource;
import it.near.sdk.jsonapiparser.annotations.Relationship;
import it.near.sdk.jsonapiparser.models.inheritance.TestParent;

public class BadRelationship extends Resource {

  @SerializedName("content")
  public String content;
  @Relationship("father")
  public TestParent father;
  @Relationship("mother")
  public TestParent mother;

  public BadRelationship() {
  }

}
