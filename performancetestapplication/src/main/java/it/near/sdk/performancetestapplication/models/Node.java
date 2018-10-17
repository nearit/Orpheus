package it.near.sdk.performancetestapplication.models;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import it.near.sdk.jsonapiparser.Resource;
import it.near.sdk.jsonapiparser.annotations.Relationship;


/**
 * Created by cattaneostefano on 21/09/16.
 */
@Keep
public class Node extends Resource {

    @Nullable
    @SerializedName("identifier")
    public String identifier;

    @SerializedName("tags")
    public List<String> tags;

    @Relationship("parent")
    public Node parent;

    @Relationship("children")
    public List<Node> children;

    public Node() {
    }

    public boolean sameIdOf(Node other) {
        if (other == null) return false;
        if (identifier == null) return other.identifier != null;
        else return identifier.equals(other.identifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return identifier != null ? identifier.equals(node.identifier) : node.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    /**
     * parent chain depth of the node, it counts the node itself.
     */
    public int parentDepth() {
        Node traveller = this;
        int depth = 0;
        while(traveller!=null) {
            depth++;
            traveller = traveller.parent;
        }
        return depth;
    }

    public boolean isBeacon() {
        return this instanceof BeaconNode &&
                this.isBeacon();
    }
}
