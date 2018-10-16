package it.near.sdk.jsonapiparser.models.inheritance;

import com.google.gson.annotations.SerializedName;

public class TestMother extends TestParent {

    @SerializedName("mom")
    public String mom;

    public TestMother() {
        super();
    }
}
