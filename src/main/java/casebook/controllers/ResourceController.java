package casebook.controllers;

import casebook.util.MimeManager;
import javache.WebConstants;
import javache.http.HttpRequest;
import javache.http.HttpResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceController extends BaseController{
    public ResourceController() {
    }


        public byte[] processResourceRequest(HttpRequest httpRequest,HttpResponse httpResponse) {
        String assetPath = WebConstants.ASSETS_FOLDER_PATH +
                httpRequest.getRequestUrl();
        File file = new File(assetPath);

        if (!file.exists() || file.isDirectory()){
            return this.notfound(("Asset not found!").getBytes(),httpResponse);
        }
        byte[] result = null;
        try{
            result = Files.readAllBytes(Paths.get(assetPath));
        }catch (Exception e){
            return this.internalServerError(("Something went wrong!").getBytes(),httpResponse);
        }
        httpResponse.addHeader("Content-Type", MimeManager.getMimeType(file.getName()));
        httpResponse.addHeader("Content-Lenght",result.length + "");
        httpResponse.addHeader("Content-Disposition","inline");
        return this.ok(result,httpResponse);
    }
}
