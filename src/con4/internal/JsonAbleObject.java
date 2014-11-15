package con4.internal;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by ekor on 15/11/2014.
 */
public abstract class JsonAbleObject {
    public String toJSON(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try{
            str = mapper.writeValueAsString(obj);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    public String toJSON(){
        return this.toJSON(this);
    }
}
