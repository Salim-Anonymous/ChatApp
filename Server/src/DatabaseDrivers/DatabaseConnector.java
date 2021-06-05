package DatabaseDrivers;

import java.sql.*;
import java.util.*;

/**
 * Collection of methods to communicate with the database
 * Bridges the communication between the server and database
 */
public class DatabaseConnector  {
    Connection userConnect;
    public DatabaseConnector() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/chatappdb";
        String user = "salim";
        String password = "salim123";
        userConnect = DriverManager.getConnection(url,user,password);

    }

    //====================user status Online========================================//

    /**
     * Updates the status of the user as online
     * @param id
     */
    public void statusOnline (String id){
        Statement updateMessage;
        try{
            updateMessage = userConnect.createStatement();
            String query = "UPDATE users SET is_active=TRUE WHERE studentID="+id+";";
            updateMessage.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error occurred in updating received date");
        }
    }

    //====================user status Offline ========================================//

    /**
     * Updates the status of the user as offline
     * @param id
     */
    public void statusOffline (String id){
        Statement updateMessage;
        try{
            updateMessage = userConnect.createStatement();
            String query = "UPDATE users SET is_active=FALSE WHERE studentID="+id+";";
            updateMessage.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error occurred in updating received date");
        }
    }
    //====================get User Details====================================//
    /**
     * fetches the details of the user by taking in the student ID
     * Dormant Function(will be in use in future updates)
     * @param studentId
     * @return
     */
    public Set<String> getUserDetails(String studentId){
        Statement getUserDetailStatement;
        try {
            getUserDetailStatement = userConnect.createStatement();
            Set<String> datum = new HashSet<>();
            try (ResultSet getSQL = getUserDetailStatement.executeQuery("SELECT * FROM users WHERE studentID="+studentId+";")){

                while(getSQL.next()){
                    datum.add(getSQL.getString("username"));
                    datum.add(getSQL.getString("email"));
                    datum.add(getSQL.getString("phone"));
                    datum.add(getSQL.getString("studentID"));
                    datum.add(getSQL.getString("department"));
                    datum.add(getSQL.getString("address"));
                }
                return datum;
            }
        } catch (SQLException | NullPointerException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //============getting password=================================//
    /**
     * Gets the ID and password of the user to compare the credentials for login
     * @return
     */
    public HashMap<String,String> getIDAndPasswords() {
        HashMap<String, String> users = new HashMap<>();
        Statement listUsers;
        try {
            listUsers = userConnect.createStatement();
            try (ResultSet getSQL = listUsers.executeQuery("SELECT studentID,password FROM users")){
                while(getSQL.next()){
                    users.put(getSQL.getString("studentID"),getSQL.getString("password"));
                }
            }
            System.out.println(users);
            return users;
        } catch (SQLException | NullPointerException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //===================getting messages between the user and the recipient=====================================//
    /**
     * Dormant Function (will be used in future updates)
     * Used to get the messages between the user and the recipient
     * @param user
     * @param recipient
     * @return
     */
    public ArrayList<String> getMessages(String user, String recipient){
        Statement getMessages;
        ArrayList<String> messages = new ArrayList<>();
        try {
            getMessages = userConnect.createStatement();
            String query = "SELECT message FROM messagesRegister WHERE sender_id="+user+" AND RecipientID="+recipient+";";
            try(ResultSet senderToRecipientMessages = getMessages.executeQuery(query)){
                while (senderToRecipientMessages.next()){
                    messages.add((senderToRecipientMessages.getString("message")));
                    //System.out.println(senderToRecipientMessages.getString("message"));
                }
            }
            return messages;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //==give a message id to the message and store in messages table and update user message id==//

    /**
     * Stores the messages between the user and the recipient
     * In the table message register.
     * @param message
     * @param senderID
     * @param recipient
     */
    public void updateUserSentMessageInDB(String message,String senderID,String recipient){
        Statement updateMessage;
        try{
            updateMessage = userConnect.createStatement();
            String query = "INSERT INTO chatappdb.messagesRegister(ReceivedAt,sender_id, message, created_at, RecipientID) VALUES(CURRENT_TIMESTAMP,'"+senderID+"','"+message+"',CURRENT_TIMESTAMP(),'"+recipient+"');";
            updateMessage.executeUpdate(query);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error occurred in updating message table");
        }

    }

    //==========================received date updater============================================//

    /**
     * updates the message date received
     * @param id
     */
    public void receivedMessage(int id){
        Statement updateMessage;
        try{
            updateMessage = userConnect.createStatement();
            String query = "UPDATE messagesRegister SET ReceivedAt=CURRENT_TIMESTAMP WHERE id="+id+";";
            updateMessage.executeUpdate(query);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error occurred in updating received date");
        }
    }

    //==========================create user=====================================================//

    /**
     * adds a new row in the user table
     * @param details to create user
     */
    public void createUser(String[] details){
        Statement createUser;
        try{
            createUser = userConnect.createStatement();
            String query = "INSERT INTO `users`(phone, email, password, username, created_at, studentID,updated_at,department,address) VALUES ('"+details[1]+"', '"+details[2]+"', '"+details[3]+"', '"+details[4]+"', CURRENT_TIMESTAMP,'"+details[5]+"',CURRENT_TIMESTAMP,'"+details[6]+"','"+details[7]+"'); ";
            createUser.executeUpdate(query);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error occurred in updating received date");
        }
    }

    //================Test Drives==================================================================//

    /*public static void main(String[] args){
        try {
            var hi = new DatabaseConnector();
            //hi.getIDAndPasswords();
            //hi.updateUserSentMessageInDB("Hello","02200163","02200228");
            //hi.receivedMessage(1);
            //hi.getMessages("02200163","02200228");
            //String[] data = {"17625152","salim123@gmail.com","Shamu123","Sangay","Shamu","02200111"};
            //hi.createUser(data);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
*/

    /**
     * Dormant Function (will be in use in future updates)
     * @bug_Fixes required for the chat_client to receive the arraylist
     * @return Online Users
     */
    public ArrayList<String> getOnline() {
        ArrayList<String> list = new ArrayList<>();
        Statement getOnlineList;
        try {
            getOnlineList = userConnect.createStatement();
            String query = "SELECT studentID FROM users WHERE is_active=1";
            try(ResultSet senderToRecipientMessages = getOnlineList.executeQuery(query)){
                while (senderToRecipientMessages.next()){
                    list.add((senderToRecipientMessages.getString("studentID")));
                }
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public String getName(String id){
        Statement getname;
        try {
            getname= userConnect.createStatement();
            String query = "SELECT username FROM users WHERE StudentID="+id+";";
            try(ResultSet name = getname.executeQuery(query)){
                if(name.next()){
                    return name.getString("username");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
