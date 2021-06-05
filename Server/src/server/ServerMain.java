package server;

import server.Server;

/**
 * The usual Driver to get the server started
 * @author salim
 */
public class ServerMain {
    public static void main(String[] args) {
        int port = 8818;
        Server server = new Server(port);
        server.start();
    }
}
