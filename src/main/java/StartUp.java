import casebook.CasebookApplication;
import javache.Server;
import javache.WebConstants;
import javache.api.RequestHandler;
import javache.http.HttpSessionStorage;
import javache.http.HttpSessionStorageImpl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StartUp {
    public static void main(String[] args) {
         start(args);

//
//        UserRepository userRepository = new UserRepository();
//        List<User>users = userRepository.finedAll();
//        userRepository.addFriend(users.get(0),users.get(1));
  //      userRepository.addFriend(users.get(1),users.get(0));

  //      BaseRepository.close();
    }

    private static HttpSessionStorage getSessionStorage() {
        return new HttpSessionStorageImpl();
    }

    private static Set<RequestHandler> initialiseApplications() {
        return new HashSet<RequestHandler>() {{
            add(new CasebookApplication(getSessionStorage()));
        }};
    }

    private static void start(String[] args) {
        int port = WebConstants.DEFAULT_SERVER_PORT;

        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        Server server = new Server(port, initialiseApplications());

        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

