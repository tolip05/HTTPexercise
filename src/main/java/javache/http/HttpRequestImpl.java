package javache.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestImpl implements HttpRequest {

    private String method;

    private String requestUrl;

    private Map<String,String> headers;

    private Map<String,String> bodyParameters;

    private Map<String,HttpCookie> cookies;

    public HttpRequestImpl(String requestContent) {
        this.initMethod(requestContent);
        this.initrequestUrl(requestContent);
        this.initHeaders(requestContent);
        this.initBodyParameters(requestContent);
        this.initCookies();
    }

    private void initMethod(String requestContent) {
     this.setMethod(requestContent.split("\\s")[0]);
    }

    private void initrequestUrl(String requestContent) {
     this.setRequestUrl(requestContent.split("\\s")[1]);
    }

    private void initHeaders(String requestContent) {
     this.headers = new HashMap<>();
        List<String>requestParams = Arrays.asList(requestContent.split("\\r\\n"));
        int i = 1;
        while (i < requestParams.size() && requestParams.get(i).length() > 0){
            String[]headerKeyValuePair = requestParams.get(i).split("\\:\\s");
            this.addHeader(headerKeyValuePair[0],headerKeyValuePair[1]);
            i++;
        }
    }

    private void initBodyParameters(String requestContent) {
      if (this.getMethod().equals("POST")){
          this.bodyParameters = new HashMap<>();
          List<String> requestParams = Arrays.asList(requestContent.split("\\r\\n"));

          if (requestParams.size() > this.headers.size() + 2){
              List<String>bodyParams = Arrays.asList(requestParams.get(this.headers.size() + 2)
                      .split("\\&"));
              for (int i = 0; i < bodyParams.size(); i++) {
                String[] bodyKeyValuePair = bodyParams.get(i).split("\\=");
                this.addBodyParameter(bodyKeyValuePair[0],bodyKeyValuePair[1]);
              }
          }

      }

    }

    private void initCookies() {
      this.cookies = new HashMap<>();
      if (!this.headers.containsKey("Cookie")){
          return;
      }
      String cookieHeader = this.headers.get("Cookie");
      String[] allCookies = cookieHeader.split("\\;\\s");

        for (int i = 0; i < allCookies.length; i++) {
            String[]cookiesKeyValuePair = allCookies[i].split("\\=");
            this.cookies.putIfAbsent(cookiesKeyValuePair[0],
                    new HttpCookieImpl(cookiesKeyValuePair[0],cookiesKeyValuePair[1]));
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public Map<String, String> getBodyParameters() {
        return this.bodyParameters;
    }

    @Override
    public Map<String, HttpCookie> getCookies() {
        return this.cookies;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(String methodName) {
      this.method = methodName;
    }

    @Override
    public String getRequestUrl() {
        return this.requestUrl;
    }

    @Override
    public void setRequestUrl(String requestUrl) {
     this.requestUrl = requestUrl;
    }

    @Override
    public void addHeader(String header, String value) {
      this.headers.putIfAbsent(header,value);
    }

    @Override
    public void addBodyParameter(String name, String parameter) {
      this.bodyParameters.putIfAbsent(name,parameter);
    }

    @Override
    public boolean isResource() {
        return this.isResource();
    }
}
