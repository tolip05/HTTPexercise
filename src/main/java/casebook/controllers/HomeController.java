package casebook.controllers;

import database.enteties.User;
import database.repositoris.UserRepository;
import javache.http.HttpRequest;
import javache.http.HttpResponse;

import java.util.HashMap;
import java.util.List;

public class HomeController extends BaseController{
    private UserRepository userRepository;

    public HomeController() {
        this.userRepository = new UserRepository();
    }

    public byte[] index(HttpRequest request, HttpResponse response){
        if (request.getCookies().containsKey(CaseBookWebConstants.CASEBOOK_SESSION_KEY)){
            return this.redirect(new byte[0],"/home",response);
        }
       return this.processPageRequest("/index",response);
    }
    public byte[] home(HttpRequest request, HttpResponse response){
        if (!request.getCookies().containsKey(CaseBookWebConstants.CASEBOOK_SESSION_KEY)){
            return this.redirect(new byte[0],"/login",response);
        }
        List<User> finedAll = this.userRepository.finedAll();

        String currentUserUsername = response.getSession().getAttributes().get("username").toString();
        HashMap<String,String>viewData = new HashMap<>();
        StringBuilder otherUsersUsername = new StringBuilder();

        for (User user : finedAll) {
            if (!user.equals(currentUserUsername)){
              otherUsersUsername.append("<h3>" + user.getName() + "</h3>");
            }
            viewData.put("username",currentUserUsername);
            viewData.put("otherUsers",otherUsersUsername.toString());
        }
        return this.processPageRequest("/home",viewData,response);
    }
}
