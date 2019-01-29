package javache.http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSessionImpl implements HttpSession {

    private String id;

    private boolean isValid;

    private Map<String,Object>attributes;

    public HttpSessionImpl() {
        this.setId(UUID.randomUUID().toString());
        this.attributes = new HashMap<>();
        this.isValid = true;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public void addAttributes(String name, Object attribute) {
      this.attributes.putIfAbsent(name,attribute);
    }

    @Override
    public void invalidate() {
        this.isValid = false;
        this.attributes = null;
    }
}
