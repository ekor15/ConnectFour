package con4.internal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ekor on 15/11/2014.
 */
public class CustomWebApplicationException extends WebApplicationException {
    public CustomWebApplicationException(String message, int status){
        super(Response.status(status).entity(message).type(MediaType.APPLICATION_JSON).build());
    }
    public CustomWebApplicationException(String message, Response.Status status){
        super(Response.status(status).entity(message).type(MediaType.APPLICATION_JSON).build());
    }
}
