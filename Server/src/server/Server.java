package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server class running on an extension to thread class to provide multiple connections
 * @author salim .
 */
public class Server extends Thread {
    private final int serverPort;
    private final ArrayList<ServerWorker> workerList = new ArrayList<>();

    /**
     * initializes the server port
     * @param serverPort
     */
    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * maintains the list of server workers to keep track of the connections
     * @return
     */
    public List<ServerWorker> getWorkerList() {
        return workerList;
    }

    /**
     * @overridded run method to start an infinite while loop to keep the server running
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true) {
                System.out.println("About to accept client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                ServerWorker worker = new ServerWorker(this, clientSocket);
                workerList.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * invoked when a user logs off or the connection is closed
     * @param serverWorker
     */
    public void removeWorker(ServerWorker serverWorker) {
        workerList.remove(serverWorker);
    }
}
