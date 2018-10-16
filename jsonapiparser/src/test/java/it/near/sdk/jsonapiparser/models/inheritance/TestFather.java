package it.near.sdk.jsonapiparser.models.inheritance;

import com.google.gson.annotations.SerializedName;

public class TestFather extends TestParent {

    @SerializedName("dad")
    public String dad;

    public TestFather() {
        super();
    }
}
