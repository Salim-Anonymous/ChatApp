package client;

import chatroom.MessageListener;
import chatroom.UserStatusListener;
import client.logging_in_Frames.Sign_In;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClient2_0 {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private OutputStream outputStream;
    private BufferedReader bufferedIn;
    private DataInputStream dataInputStream;
    private ObjectInputStream objectInputStream;
    private final ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
    private final ArrayList<MessageListener> messageListeners = new ArrayList<>();

    /**
     *
     * @param serverName
     * @param serverPort
     */
    public ChatClient2_0(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    /**
     *
     * @param sendTo
     * @param msgBody
     * @throws IOException
     */
    public void msg(String sendTo, String msgBody) throws IOException {
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        outputStream.write(cmd.getBytes());
    }

    /**
     *
     * @param details
     * @throws IOException
     */
    public void createNewAccount(String[] details) throws IOException {
        String cmd ="create"+" "+details[0]+" "+details[1]+" "+details[2]+" "+details[3]+" "+details[4]+" "+details[5]+" "+details[6]+"\n";
        outputStream.write(cmd.getBytes());
    }
     /**
         *
         * @return
         * @throws IOException
        */
        public DefaultListModel<String> getOnline() throws IOException {
            outputStream.flush();
            String cmd = "getOnline"+"\n";
            outputStream.write(cmd.getBytes());
            ArrayList<String>hi = new ArrayList<>();
            DefaultListModel<String> hello = new DefaultListModel<>();
            String line;
            while((line = bufferedIn.readLine())!=null){
                if(line.equals("esc")){
                    break;
                }
                System.out.println("Second Data Received");
                hello.addElement(line);
            }
            if(!hello.isEmpty()){
                System.out.println("Test 1 passed");

            }
            return hello;
        }
    /**
     *
     * @param user
     * @param recipient
     * @return
     * @throws IOException
     */
    public DefaultListModel<String> getMessageList(String user, String recipient) throws IOException {
        DefaultListModel<String> model = new DefaultListModel<>();
        String cmd = "getMsg"+" "+user+" "+recipient+"\n";
        outputStream.flush();
        outputStream.write(cmd.getBytes());
        String line;
        while((line = bufferedIn.readLine())!=null){
                if(line.equals("esc")){
                    break;
                }
                System.out.println("Message Data Received");
                model.addElement(line);
            }
            if(!model.isEmpty()){
                System.out.println("Test 1 passed");
            }
        return model;
    }

    /**
     *
     * @param login
     * @param password
     * @return
     * @throws IOException
     */
    public boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        outputStream.write(cmd.getBytes());
        String response  = bufferedIn.readLine();
        System.out.println("Response Line:" + response);
        if ("logged in".equalsIgnoreCase(response)) {
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     */
    private void startMessageReader() {
        Thread t = new Thread(this::readMessageLoop);
        t.start();
    }

    /**
     *
     */
    private void readMessageLoop() {
        try {
            String line;

            while ((line = bufferedIn.readLine()) != null) {
                String[] tokens = StringUtils.split(line);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)) {
                        handleOnline(tokens);
                    } else if ("offline".equalsIgnoreCase(cmd)) {
                        handleOffline(tokens);
                    } else if ("msg".equalsIgnoreCase(cmd)) {
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        handleMessage(tokensMsg);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param tokensMsg
     */
    private void handleMessage(String[] tokensMsg) {
        String login = tokensMsg[1];
        String msgBody = tokensMsg[2];

        for(MessageListener listener : messageListeners) {
            listener.onMessage(login, msgBody);
        }
    }

    /**
     *
     * @param tokens
     */
    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }

    /**
     *
     * @param tokens
     */
    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.online(login);
        }
    }

    /**
     *
     * @return
     */
    public boolean connect() {
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Client port is " + socket.getLocalPort());
            outputStream = socket.getOutputStream();
            bufferedIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



    /**
     *
     * @param listener
     */
    public void addUserStatusListener(UserStatusListener listener) {
        userStatusListeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeUserStatusListener(UserStatusListener listener) {
        userStatusListeners.remove(listener);
    }

    /**
     *
     * @param listener
     */
    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    /**
     *
     * @throws IOException
     */
    public void logoff() throws IOException {
        String cmd = "logoff\n";
        outputStream.flush();
        outputStream.write(cmd.getBytes());
        new Sign_In();
    }

    public String getname(String id) {
        String line;

        try {
            outputStream.flush();
            String cmd = "getName "+id;
            outputStream.write(cmd.getBytes());
            if ((line = bufferedIn.readLine())!=null){
                System.out.println("Message Data Received");
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
