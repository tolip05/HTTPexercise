package javache.api;

public interface RequestHandler {
    byte[] handleRequest(String content);

    boolean hasIntercepted();
}
