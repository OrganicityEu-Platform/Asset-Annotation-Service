package eu.oc.annotations.handlers;



/**
 * Created by Evangelos on 5/16/2015.
 */
public class RestException extends IllegalStateException {
    public RestException(String message) {
        super("Error Message:"+message);
    }
}
