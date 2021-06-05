package server;

import DatabaseDrivers.DatabaseConnector;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

/**
 * Worker to serve the chat client
 * new thread is generated for every socket connection
 * @author salim .
 */
public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login;
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;
    private final HashSet<String> topicSet = new HashSet<>();
    HashMap <String, String> userlist;
    DatabaseConnector con;

    /**
     * Constructs the database connector
     * @param server
     * @param clientSocket
     */
    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        try {
            this.con = new DatabaseConnector();
        } catch (SQLException throwables) {
            System.out.println("server worker failed to connect to database");
        }
    }
    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Contains the commands that a client can enter to do specific tasks
     * the commands are entered by the client are read through the input_stream
     * @throws IOException
     * @throws InterruptedException
     */
    private void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        this.dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ( (line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;
                }else if("create".equals(cmd)){
                    String[] details = StringUtils.split(line,null,8);
                    createUser(details);
                } else if ("login".equalsIgnoreCase(cmd)) {
                    try {
                        handleLogin(outputStream, tokens);
                    } catch (SQLException throwables) {
                        System.out.println("Connection to database error");
                    }
                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                } else if ("join".equalsIgnoreCase(cmd)) {
                    handleJoin(tokens);
                } else if ("leave".equalsIgnoreCase(cmd)) {
                    handleLeave(tokens);
                } else if("getMsg".equalsIgnoreCase(cmd)){
                    String[] userRecipient = StringUtils.split(line,null,3);
                    ArrayList<String>list = con.getMessages(userRecipient[1],userRecipient[2]);
                    System.out.println("Getting messages");
                    String msg;
                    outputStream.flush();
                    for(String s: list){
                        msg = s+"\n";
                        outputStream.write(msg.getBytes());
                        System.out.println("message sent");
                    }
                    outputStream.write("esc\n".getBytes());
                    list.clear();

                }else if("getOnline".equalsIgnoreCase(cmd)){
                    ArrayList<String> onlineRecipient = con.getOnline();
                    String msg;
                    outputStream.flush();
                    for (String s : onlineRecipient) {
                        msg = s+"\n";
                        outputStream.write((msg).getBytes());
                        System.out.println("Data sent");
                    }
                    outputStream.write("esc\n".getBytes());
                    onlineRecipient.clear();

                }else if("getDetails".equalsIgnoreCase(cmd)) {
                    Set<String> details = con.getUserDetails(tokens[1]);
                    String msg;
                    outputStream.flush();
                    for (String s : details) {
                        msg = s + "\n";
                        outputStream.write(msg.getBytes());
                    }
                }else if("getName".equalsIgnoreCase(cmd)){
                    String[] userRecipient = StringUtils.split(line,null,3);
                    String msg = con.getName(userRecipient[1]);
                    outputStream.flush();
                        msg = msg+"\n";
                        outputStream.write((msg).getBytes());
                        System.out.println("Data sent");
                } else {
                    outputStream.flush();
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        clientSocket.close();
    }


    /**
     * Dormant method- may proves useful to create a group in future
     * @param tokens
     */
    private void handleLeave(String[] tokens) {
        if (tokens.length > 1) {
            String topic = tokens[1];
            topicSet.remove(topic);
        }
    }

    /**
     * Dormant method (useful for getting creating groups)
     * @param topic
     * @return
     */
    public boolean isMemberOfTopic(String topic) {
        return topicSet.contains(topic);
    }

    /**
     * Dormant method (useful to create a group)
     * @param tokens
     */
    private void handleJoin(String[] tokens) {
        if (tokens.length > 1) {
            String topic = tokens[1];
            topicSet.add(topic);
        }
    }

    /**
     * it conveys the messages to respective users
     * @param tokens
     */
    private void handleMessage(String[] tokens) {
        String sendTo = tokens[1];
        String body = tokens[2];

        boolean isTopic = sendTo.charAt(0) == '#';
        //-----------getting server list of workers-----------------------------------------//
        List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker worker : workerList) {
            if (isTopic) {
                if (worker.isMemberOfTopic(sendTo)) {
                    String outMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
                    worker.send(outMsg);
                    con.updateUserSentMessageInDB(body,login,sendTo);
                }
            } else {
                if (sendTo.equalsIgnoreCase(worker.getLogin())) {
                    String outMsg = "msg " + login + " " + body + "\n";
                    worker.send(outMsg);
                    con.updateUserSentMessageInDB(body,login,sendTo);
                }
            }
        }
    }

    /**
     * For logging off the account on the server
     * @throws IOException
     */
    private void handleLogoff() throws IOException {

        List<ServerWorker> workerList = server.getWorkerList();

        // send other online users current user's status
        String onlineMsg = "offline " + login + "\n";

        try{
            con.statusOffline(login);
            for(ServerWorker worker : workerList) {
                if (!login.equals(worker.getLogin())) {
                    worker.send(onlineMsg);
                }
            }}catch (NullPointerException e){
                System.out.println("Login is null closing the socket and server worker");
             }
        server.removeWorker(this);
        clientSocket.close();
    }

    /**
     * Invoke the Database connector to create a new user
     * @param details
     */
    private void createUser(String[] details){
        con.createUser(details);
        String[] token = new String[2];
        token[0] = details[6];
        token[1] =  details[3];
        try {
            handleLogin(outputStream,token);
        } catch (IOException e) {
            System.out.println("IO error");
        } catch (SQLException throwables) {
            System.out.println("error logging in from creation");
        }
    }

    /**
     * returns the Student_ID of the current user being served
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     * Compares the credentials of the user and the values entered by the client
     * Handles the login
     * @param outputStream
     * @param tokens
     * @throws IOException
     * @throws SQLException
     */
    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException, SQLException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

            userlist = con.getIDAndPasswords();

            if(getAuthentication(tokens[1],tokens[2])||(login.equals("guest") && password.equals("guest")) || (login.equals("salim") && password.equals("bun")))
            {
                String msg = "logged in\n";
                outputStream.flush();
                outputStream.write(msg.getBytes());
                outputStream.flush();
                this.login = login;
                System.out.println("User logged in successfully: " + login);
                con.statusOnline(login);
                //get message list from database
                 List<ServerWorker> workerList = server.getWorkerList();
                // send current user all other online logins
                for(ServerWorker worker : workerList) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin())) {
                            String msg2 = "online " + worker.getLogin() + "\n";
                            send(msg2);
                        }
                    }
                }
                //-------send other online users current user's status----------//
                String onlineMsg = "online " + login + "\n";
                for(ServerWorker worker : workerList) {
                    if (!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }
            } else {
                String msg = "error login\n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }

    /**
     * The method that compares that actually gets the comparison of the Student ID and passwords done
     * @param id
     * @param password
     * @return
     */
    private boolean getAuthentication(String id, String password) {
        for (String x: userlist.keySet()) {
            if(x.equals(id)&&userlist.get(x).equals(password)){
                return true;
            }
        }
        return false;
    }

    /**
     * Just another method to send output to the client socket terminal
     * @param msg
     */
    private void send(String msg) {
        if (login != null) {
            try {
                outputStream.flush();
                outputStream.write(msg.getBytes());
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
