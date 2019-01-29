package casebook.controllers;

import database.enteties.User;
import database.repositoris.UserRepository;
import javache.http.HttpRequest;
import javache.http.HttpResponse;
import javache.http.HttpSession;
import javache.http.HttpSessionImpl;

import java.util.Date;

public class UserController extends BaseController {
    private UserRepository userRepository;

    public UserController() {
        this.userRepository = new UserRepository();
    }

    public byte[] login(HttpRequest request, HttpResponse response) {
        if (request.getCookies().containsKey(CaseBookWebConstants.CASEBOOK_SESSION_KEY)) {
            return this.redirect(new byte[0], "/home", response);
        }
        return this.processPageRequest("/login", response);
    }

    public byte[] register(HttpRequest request, HttpResponse response) {
        if (request.getCookies().containsKey(CaseBookWebConstants.CASEBOOK_SESSION_KEY)) {
            return this.redirect(new byte[0], "/home", response);
        }
        return this.processPageRequest("/register", response);
    }

    public byte[] loginPost(HttpRequest request, HttpResponse response) {
        if (request.getCookies().containsKey(CaseBookWebConstants.CASEBOOK_SESSION_KEY)) {
            return this.redirect(new byte[0], "/home", response);
        }

        String username = request.getBodyParameters().get("username");
        String password = request.getBodyParameters().get("password");

        if (this.userRepository.finedByUsername(username) == null){
            return this.badRequest("Username does not exist".getBytes(),response);
        }

        HttpSession session = new HttpSessionImpl();

        session.addAttributes("username",username);

        response.addCookie(CaseBookWebConstants.CASEBOOK_SESSION_KEY,session.getId());
        response.setSession(session);

        return this.redirect("Successfuly login".getBytes(), "/home", response);
    }

    public byte[] registerPost(HttpRequest request, HttpResponse response) {
        if (request.getCookies().containsKey(CaseBookWebConstants.CASEBOOK_SESSION_KEY)) {
            return this.redirect(new byte[0], "/home", response);
        }

        if (!request.getBodyParameters().get("password")
                .equals(request.getBodyParameters().get("confirmPassword"))) {
            return this.badRequest(("Password do not match!").getBytes(), response);
        }
        User user = new User();

        user.setName(request.getBodyParameters().get("username"));
        user.setPassword(request.getBodyParameters().get("password"));
        this.userRepository.save(user);

        return this.redirect(String.format("Successfuly registered user %s",
                user.getName()).getBytes(), "/login", response);
    }
    public byte[] logout(HttpRequest request,HttpResponse response){
        if (!request.getCookies().containsKey(CaseBookWebConstants.CASEBOOK_SESSION_KEY)){
            return this.redirect(new byte[0],"/login",response);
        }
        response
                .addCookie(CaseBookWebConstants.CASEBOOK_SESSION_KEY,
                        "removed; expires=" + new Date(0).toString());
        response.getSession().invalidate();
        return this.redirect("Successfuly logout".getBytes(), "/", response);
    }
}
