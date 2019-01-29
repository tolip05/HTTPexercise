package javache.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseImpl implements HttpResponse {



    private HttpStatus httpStatus;
    private HttpSession session;
    private Map<String, String> headers;
    private Map<String, HttpCookie> cookies;
    private byte[] content;

    public HttpResponseImpl() {
      this.setContent(new byte[0]);
      this.setSession(null);
      this.headers = new HashMap<>();
      this.cookies = new HashMap<>();
    }

    private byte[] getHeadersBytes(){
        StringBuilder result = new StringBuilder();
      result
              .append(ResponseLines.getResponseLine(this.getStatusCode().getStatusCode()))
              .append(System.lineSeparator());
        for (Map.Entry<String, String> entry : this.getHeaders().entrySet()) {
            result.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(System.lineSeparator());
        }
        if (!this.cookies.isEmpty()){
            result.append("Set-Cookie: ");

            for (HttpCookie value : cookies.values()) {
                result.append(value.toString()).append("; ");
            }
            result.replace(result.length() - 2,result.length(),"");
            result.append(System.lineSeparator());
        }
        result.append(System.lineSeparator());
    return result.toString().getBytes();
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public HttpStatus getStatusCode() {
        return this.httpStatus;
    }

    @Override
    public byte[] getContent() {
        return this.content;
    }



    @Override
    public void setStatusCode(HttpStatus statusCode) {
        this.httpStatus = statusCode;
    }

    @Override
    public void setContent(byte[] content) {
       this.content = content;
    }

    @Override
    public void addHeader(String header, String value) {
     this.headers.putIfAbsent(header,value);
    }

    @Override
    public void addCookie(String name, String value) {
      this.cookies.putIfAbsent(name,new HttpCookieImpl(name,value));
    }

    @Override
    public HttpSession getSession() {
        return this.session;
    }

    @Override
    public void setSession(HttpSession session) {
       this.session = session;
    }

    @Override
    public byte[] getBytes() {
        byte[] headarBytes = this.getHeadersBytes();
        byte[] bodyBytes = this.getContent();
        byte[] fullResponse = new byte[headarBytes.length + bodyBytes.length];
        System.arraycopy(headarBytes,0,fullResponse,0,headarBytes.length);
        System.arraycopy(bodyBytes,0,fullResponse,headarBytes.length,bodyBytes.length);
        return fullResponse;
    }
}
