package it.near.sdk.jsonapiparser.exceptions;

/**
 * Created by raphaelseher on 09/03/16.
 */
public class NotExtendingResourceException extends Exception {
    public NotExtendingResourceException(String detailMessage) {
        super(detailMessage);
    }
}
