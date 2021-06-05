package chatroom;

/**
 * @author Salim
 */
public interface MessageListener {
    void onMessage(String fromLogin, String msgBody);

}
