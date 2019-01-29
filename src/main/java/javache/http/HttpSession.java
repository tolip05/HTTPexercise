package javache.http;

import java.util.Map;

public interface HttpSession {
    String getId();

     boolean isValid();

     Map<String,Object>getAttributes();

     void addAttributes(String name,Object attribute);

     void invalidate();
}
