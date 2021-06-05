package chatroom;

import client.ChatClient2_0;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Salim Pradhan
*/
public class ChatroomUI extends JFrame implements UserStatusListener,MessageListener{

	@Serial
    private static final long serialVersionUID = 1L;
	private final client.ChatClient2_0 client;
    private DefaultListModel<String>userListModel = new DefaultListModel<>(); //model for the userlistUI
    private DefaultListModel<String> listModel; //model for the messageList

    private JList<String> userListUI;// displays the list of users on the screen
    private JTextField textArea;
    private JList<String> messageList; // displays the list of messages of users on screen
    private final ArrayList<String> usersOnline = new ArrayList<>(); // stores the users that are online
    private final String login;
    HashMap<String,DefaultListModel<String>> userMessages = new HashMap<>(); // stores the listModels for differnt users
    private JLabel userName; // changing label based on the user selected

    /**
     * Main UI to chat

     * @param client
     */
    public ChatroomUI(ChatClient2_0 client,String log) {

        this.client = client;
        this.client.addUserStatusListener(this);
        this.client.addMessageListener(this);
        this.login = log;
        initComponents();
        setTitle("Feather: "+login);
        setResizable(false);
        setVisible(true);

    }

    /**
     * same old initialisation of the window
     */
    private void initComponents() {

        JPanel userBoard = new JPanel();
        JLabel userLabel = new JLabel();

        try {
            userListModel = client.getOnline();
            if(userListModel.contains(login)){
                userListModel.removeElement(login);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        userListUI = new JList<>(userListModel);
        userName = new JLabel();
        JPanel messageBoard = new JPanel();
        JLabel messageLabel = new JLabel();
        textArea = new JTextField();
        JButton sendButton = new JButton();
        listModel = new DefaultListModel<>();
        messageList = new JList<>(listModel);


        //======== this ========
        setBackground(new Color(153, 102, 255));
        setForeground(new Color(153, 102, 255));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== userBoard ========
        JScrollPane chatScroll;
        {
            userBoard.setForeground(new Color(148, 138, 153));
            userBoard.setBackground(new Color(173, 169, 169));
            userBoard.setLayout(null);

            //---- userLabel ----
            userLabel.setText("Online Users:");
            userLabel.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/pictures/ezgif.com-gif-maker.gif"))));
            userLabel.setBackground(new Color(153, 102, 255));
            userLabel.setForeground(Color.black);
            userLabel.setFont(new Font("Yrsa", Font.BOLD, 14));
            userBoard.add(userLabel);
            userLabel.setBounds(15, 10, 120, 20);

            //======== chatScroll ========
            {
                chatScroll = new JScrollPane(userListUI);
                //---- userListUI ----
                userListUI.setFont(new Font("Yrsa", Font.ITALIC, 16));
                userListUI.setVisibleRowCount(44);
                userListUI.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) { userListUIMouseClicked(e);
                    }
                });
            }
            userBoard.add(chatScroll);
            chatScroll.setBounds(10, 45, 120, 345);
        }
        JButton logoutButton = new JButton();
        logoutButton.setText("Logout");
        logoutButton.setBackground(new Color(241, 241, 246));
        logoutButton.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 16));
        logoutButton.setForeground(Color.black);
        logoutButton.setToolTipText("");
        logoutButton.setBorder(new LineBorder(Color.black,1,true));
        logoutButton.addActionListener(e -> logoutButtonActionPerformed());
        //userBoard.add(logoutButton);
        logoutButton.setBounds(440,13,60,20);
        userBoard.add(chatScroll);
        contentPane.add(userBoard);
        userBoard.setBounds(10, 5, 150, 415);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);

        }
        //======== messageBoard ========
            {
                messageBoard.setBackground(new Color(149, 146, 153));
                messageBoard.setLayout(null);
                messageBoard.add(logoutButton);
                //---- textArea ----
                textArea.setBackground(Color.white);
                textArea.setFont(new Font("Yrsa", Font.PLAIN, 12));
                textArea.setBorder(new LineBorder(Color.black,1,true));
                textArea.setText("enter message");
                textArea.setForeground(Color.black);
                textArea.addActionListener(this::textAreaActionPerformed);
                messageBoard.add(textArea);
                textArea.setBounds(10, 365, 440, textArea.getPreferredSize().height);
                 //---- sendButton ----
                sendButton.setText("");
                sendButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/pictures/send-button.png"))));
                sendButton.setBorder(new LineBorder(Color.black, 1, true));
                sendButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        sendButtonMouseClicked(e);
                    }
                });
                messageBoard.add(sendButton);
                sendButton.setBounds(460,365,30,textArea.getPreferredSize().height);


                //======== messageScroll ========
                JScrollPane messageScroll;
                {
                    messageScroll = new JScrollPane(messageList);
                    messageScroll.setBorder(new LineBorder(Color.black,1,true));

                }
                messageBoard.add(messageScroll);
                messageScroll.setBounds(10, 55, 500, 305);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < messageBoard.getComponentCount(); i++) {
                        Rectangle bounds = messageBoard.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = messageBoard.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    messageBoard.setMinimumSize(preferredSize);
                    messageBoard.setPreferredSize(preferredSize);
                }
                userName.setText("User");
                userName.setHorizontalAlignment(SwingConstants.CENTER);
                userName.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 20));
                userName.setBackground(Color.white);
                userName.setBorder(new LineBorder(Color.black, 1, true));
                messageBoard.add(userName);
                userName.setBounds(30, 15, 295, 25);
            }

            contentPane.add(messageBoard);
            messageBoard.setBounds(160, 5, 520, 415);

        setSize(690,470);
        setLocationRelativeTo(getOwner());
    }


    /**
     * handles the send Button
     * @param e
     */
    private void sendButtonMouseClicked(MouseEvent e) {
        if (e.getClickCount()>0){
            try {
            String text = textArea.getText();
            client.msg(userListUI.getSelectedValue(), text);
            listModel.addElement("You: " + text);
            textArea.setText("");
          } catch (IOException e1) {
            e1.printStackTrace();
        }
        }
    }

    /**
     * handles the logout button
     */
    private void logoutButtonActionPerformed() {
        try {
            client.removeMessageListener(this);
            client.removeUserStatusListener(this);
            client.logoff();
            this.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handles the event when a user is selected form the userListUI list
     * @param e
     */
    private void userListUIMouseClicked(MouseEvent e) {
        userName.setText(userListUI.getSelectedValue());
        if (e.getClickCount() > 0) {
            if(!userMessages.containsKey(userListUI.getSelectedValue())){
                userMessages.put(userListUI.getSelectedValue(),new DefaultListModel<>());
            }
            listModel = userMessages.get(userListUI.getSelectedValue());
            messageList.setModel(listModel);
        }
    }

    /**
     * handles the event when VK_ENTER is pressed
     * @param e
     */
    private void textAreaActionPerformed(ActionEvent e) {
        try {
            String text = textArea.getText();
            client.msg(userListUI.getSelectedValue(), text);
            listModel.addElement("You => " + text);
            textArea.setText("");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * overridden method that uploads the messages received to the respective models
     * @param fromLogin
     * @param msgBody
     */
    @Override
    public void onMessage(String fromLogin, String msgBody) {

        for (String x:usersOnline) {
            if (x.equalsIgnoreCase(fromLogin)) {
            String line = fromLogin + "=< " + msgBody;
            System.out.println("passed");
            if(userMessages.containsKey(fromLogin)){
                userMessages.get(fromLogin).addElement(line);
            }
        }
        }
    }

    /**
     * @Override methods to handle when a user comes online
     * @param recipient
     */
    @Override
    public void online(String recipient) {

        if(!userListModel.contains(recipient)){
            userMessages.put(recipient, new DefaultListModel<>());
            userListModel.addElement(recipient);
            usersOnline.add(recipient);
        }
    }

    /**
     * function is Dormant For now...
     * @param id
     * @return
     */
    public String getName(String id){
        return client.getname(id);
    }

    /**
     * Handles the event when a user goes offline
     * @remove User from UserListUI model
     * @param login
     */
    @Override
    public void offline(String login) {
        userListModel.removeElement(login);
    }
    /*
      public static void main(String[]args){
     client.ChatClient2_0 client = new client.ChatClient2_0("localhost", 8818);
     chatroom.ChatroomUI chatroomUI = new chatroom.ChatroomUI(client);
        chatroomUI.setVisible(true);
        if (client.connect()) {
            try {
                client.login("guest", "guest");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

}
