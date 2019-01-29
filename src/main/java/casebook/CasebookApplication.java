package casebook;

import casebook.controllers.CaseBookWebConstants;
import casebook.controllers.HomeController;
import casebook.controllers.ResourceController;
import casebook.controllers.UserController;
import casebook.util.ControllerActionPair;
import javache.WebConstants;
import javache.api.RequestHandler;
import javache.http.*;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CasebookApplication implements RequestHandler {

    private boolean intercepted;
    private HttpResponse httpResponse;
    private HttpRequest httpRequest;
    private HttpSessionStorage sessionStorage;
    private Map<String, ControllerActionPair> getRequestMapingRouth;
    private Map<String, ControllerActionPair> postRequestMapingRouth;

    public CasebookApplication(HttpSessionStorage sessionStorage) {
        this.intercepted = false;
        this.sessionStorage = sessionStorage;

        this.initializeGetRoutingMap();
        this.initializePostRoutingMap();
    }

    private void initializeGetRoutingMap() {
        try {
            this.getRequestMapingRouth = new HashMap<>() {{

                put("/", new ControllerActionPair(
                        new HomeController(), HomeController.class
                        .getDeclaredMethod("index",
                                HttpRequest.class, HttpResponse.class)
                ));
                put("/login", new ControllerActionPair(
                        new UserController(), UserController.class
                        .getDeclaredMethod("login",
                                HttpRequest.class, HttpResponse.class)
                ));
                put("/register", new ControllerActionPair(
                        new UserController(), UserController.class
                        .getDeclaredMethod("register",
                                HttpRequest.class, HttpResponse.class)
                ));
                put("/logout", new ControllerActionPair(
                        new UserController(), UserController.class
                        .getDeclaredMethod("logout",
                                HttpRequest.class, HttpResponse.class)
                ));
                put("/home", new ControllerActionPair(
                        new HomeController(), HomeController.class
                        .getDeclaredMethod("home",
                                HttpRequest.class, HttpResponse.class)
                ));

            }};
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    private void initializePostRoutingMap() {
        try {
            this.postRequestMapingRouth = new HashMap<>() {{
                put("/login", new ControllerActionPair(
                        new UserController(), UserController.class
                        .getDeclaredMethod("loginPost", HttpRequest.class, HttpResponse.class)
                ));
                put("/register", new ControllerActionPair(
                        new UserController(), UserController.class
                        .getDeclaredMethod("registerPost", HttpRequest.class
                                , HttpResponse.class)
                ));

            }};
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    public byte[] handleRequest(String requestHandler) {
        this.httpRequest = new HttpRequestImpl(requestHandler);
        this.httpResponse = new HttpResponseImpl();
        if (this.httpRequest.getCookies()
                .containsKey(CaseBookWebConstants.CASEBOOK_SESSION_KEY)){
            HttpSession clientSession = this.sessionStorage
                    .getById(this.httpRequest.getCookies()
                    .get(CaseBookWebConstants.CASEBOOK_SESSION_KEY).getValue());
            this.httpResponse.setSession(clientSession);
        }

        byte[] result = null;

        if (this.httpRequest.getMethod().equals("GET")) {
            result = this.processGetRequest();
        } else if (this.httpRequest.getMethod().equals("POST")) {
            result = this.processPostRequest();
        }
        if (this.httpResponse.getSession() != null &&
                this.sessionStorage.getById
                        (this.httpResponse.getSession().getId()) == null) {
            this.sessionStorage.addSession(this.httpResponse.getSession());
        }
        this.sessionStorage.refreshSessions();
        this.intercepted = true;
        return result;
    }

    private byte[] processPostRequest() {
        String requestUrl = this.httpRequest.getRequestUrl();
        if (this.postRequestMapingRouth.containsKey(requestUrl)) {
            ControllerActionPair cap =
                    this.postRequestMapingRouth.get(requestUrl);
            try {
                return (byte[]) cap.getAction()
                        .invoke(cap.getBaseController()
                                , this.httpRequest
                                , this.httpResponse);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        throw new IllegalArgumentException("You should reach here");
    }

    @Override
    public boolean hasIntercepted() {
        return true;
    }


    private byte[] processGetRequest() {
        String requestUrl = this.httpRequest.getRequestUrl();
        if (this.getRequestMapingRouth.containsKey(requestUrl)) {
            ControllerActionPair cap =
                    this.getRequestMapingRouth.get(requestUrl);
            try {
                return (byte[]) cap.getAction()
                        .invoke(cap.getBaseController(), this.httpRequest, this.httpResponse);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return new ResourceController()
                .processResourceRequest(this.httpRequest, this.httpResponse);
    }


}
