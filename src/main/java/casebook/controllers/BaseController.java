package casebook.controllers;

import javache.WebConstants;
import javache.http.HttpRequest;
import javache.http.HttpResponse;
import javache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
private static final String PAGE_MYME_TYPE = "text/html";

    protected BaseController() {

    }

    protected byte[] redirect(byte[] content, String location,HttpResponse httpResponse) {
        httpResponse.setStatusCode(HttpStatus.SeeOther);
        httpResponse.addHeader("Location",location);
        httpResponse.setContent(content);
        return httpResponse.getBytes();
    }

    protected byte[] ok(byte[] content,HttpResponse httpResponse) {
        httpResponse.setStatusCode(HttpStatus.Ok);
        httpResponse.setContent(content);
        return httpResponse.getBytes();
    }
    protected byte[] created(byte[] content,HttpResponse httpResponse) {
        httpResponse.setStatusCode(HttpStatus.Created);
        httpResponse.setContent(content);
        return httpResponse.getBytes();
    }
    protected byte[] badRequest(byte[] content,HttpResponse httpResponse){
        httpResponse.setStatusCode(HttpStatus.BadRequest);
        httpResponse.setContent(content);
        return httpResponse.getBytes();
    }
    protected byte[] notfound(byte[] content,HttpResponse httpResponse){
        httpResponse.setStatusCode(HttpStatus.NotFound);
        httpResponse.setContent(content);
        return httpResponse.getBytes();
    }
    protected byte[] internalServerError(byte[] content,HttpResponse httpResponse) {
        httpResponse.setStatusCode(HttpStatus.InternalServerError);
        httpResponse.setContent(content);
        return httpResponse.getBytes();
    }


    protected byte[] processPageRequest(String page,HttpResponse httpResponse){
        return processPageRequest(page,null,httpResponse);
    }

    protected byte[] processPageRequest(String page, HashMap<String,String> viewData, HttpResponse httpResponse) {
        String pagePath = WebConstants.PAGES_FOLDER_PATH +
                page
                + CaseBookWebConstants.HTML_EXTENSION_AND_SEPARATOR;

        File file = new File(pagePath);

        if(!file.exists() || file.isDirectory()) {
            return this.notfound(("Page not found!").getBytes(),httpResponse);
        }
        byte[] result = null;
        try {
            if(viewData != null){
                result = this.loadAndRenderPage(pagePath,viewData);
            }else {
                result = Files.readAllBytes(Paths.get(pagePath));
            }
        } catch (IOException e) {
            return this.internalServerError(("Something went wrong!").getBytes(),httpResponse);
        }

        httpResponse.addHeader("Content-Type", PAGE_MYME_TYPE);

        return this.ok(result,httpResponse);
    }
    private byte[] loadAndRenderPage(String pagePath,HashMap<String,String>viewData) throws IOException {
        String textContent = String.join("",Files.readAllLines(Paths.get(pagePath)));

        for (Map.Entry<String, String> entry : viewData.entrySet()) {
            textContent = textContent
                    .replace("${" + entry.getKey() + "}",entry.getValue());
        }
        return textContent.getBytes();
    }
}
