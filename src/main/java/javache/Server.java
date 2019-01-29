package javache;

import javache.api.RequestHandler;
import javache.http.HttpSessionStorage;
import javache.http.HttpSessionStorageImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.concurrent.FutureTask;

public class Server {
    private static final String LISTENING_MESSAGE = "Listening on port: ";

    private static final String TIMEOUT_DETECTION_MESSAGE = "Timeout detected!";

    private static final Integer SOCKET_TIMEOUT_MILLISECONDS = 5000;

    private int port;

    private int timeouts;

    private ServerSocket socket;

    private Set<RequestHandler> requestHandlers;

    public Server(int port,Set<RequestHandler>requestHandlers) {
        this.port = port;
        this.timeouts = 0;
        this.requestHandlers = requestHandlers;
    }
    public void run() throws IOException {
        this.socket = new ServerSocket(this.port);
        System.out.println(LISTENING_MESSAGE + this.port);
        this.socket.setSoTimeout(SOCKET_TIMEOUT_MILLISECONDS);
        HttpSessionStorage serverSessionStorage = new HttpSessionStorageImpl();

        while (true){
            try(Socket clientSocket = this.socket.accept()){
            clientSocket.setSoTimeout(SOCKET_TIMEOUT_MILLISECONDS);
                ConnectionHandler connectionHandler =
                        new ConnectionHandler(clientSocket,this.requestHandlers);

                FutureTask<?>task = new FutureTask<>(connectionHandler,null);
                task.run();
            }catch (SocketTimeoutException e){
                System.out.println(TIMEOUT_DETECTION_MESSAGE);
                this.timeouts++;
            }
        }

    }
}
