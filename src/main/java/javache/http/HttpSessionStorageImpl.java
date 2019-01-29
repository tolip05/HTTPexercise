package javache.http;

import java.util.*;

public class HttpSessionStorageImpl implements HttpSessionStorage {

    private Map<String, HttpSession> allSessions;


    public HttpSessionStorageImpl() {
        this.allSessions = new HashMap<>();
    }

    @Override
    public HttpSession getById(String sessionId) {
        if (!this.allSessions.containsKey(sessionId)) {
            return null;
        }
        return this.allSessions.get(sessionId);
    }

    public Map<String,HttpSession>getAllSessions(){
        return Collections.unmodifiableMap(this.allSessions);
    }
    @Override
    public void addSession(HttpSession session) {
        this.allSessions.putIfAbsent(session.getId(), session);
    }

    @Override
    public void refreshSessions() {
        List<String> isToRemove = new ArrayList<>();

        for (HttpSession value : allSessions.values()) {
            if (!value.isValid()){
                isToRemove.add(value.getId());
        }
            for (String id : isToRemove) {
                this.allSessions.remove(id);
            }
        }
    }
}
