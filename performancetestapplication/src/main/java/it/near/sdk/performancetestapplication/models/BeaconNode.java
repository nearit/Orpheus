package it.near.sdk.performancetestapplication.models;

import android.support.annotation.Keep;



import java.util.UUID;

@Keep
public class BeaconNode extends Node {
    /*@SerializedName("proximity_uuid")
    public String proximityUUID;

    @SerializedName("major")
    public Integer major;

    @SerializedName("minor")
    public Integer minor;

    public BeaconNode() {
    }

    public Region toAltRegion() throws NullPointerException {
        if (this.identifier == null || this.minor != null)
            throw new NullPointerException();
        return new Region(this.getId(),
                Identifier.fromUuid(UUID.fromString(this.proximityUUID)),
                this.major != null ? Identifier.fromInt(this.major) : null,
                this.minor != null ? Identifier.fromInt(this.minor) : null);
    }

    public boolean isBeacon() {
        return this.minor != null && this.major != null;
    }*/
}
