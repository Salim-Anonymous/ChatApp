package client.logging_in_Frames;

import chatroom.ChatroomUI;
import client.ChatClient2_0;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Well its the JFrame for sign in window
 * @author Salim
 */
public class Sign_In extends JFrame {
    ChatClient2_0 client;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JWindow credentialError;
    private JWindow logging_in;

    /**
     * The usual constructor
     * instantiates the chatclient
     * Inits the window
     */
    public Sign_In() {
        client = new client.ChatClient2_0("localhost", 8818);
        if (client.connect()) {
            initComponents();
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    /**
     * Failed attempt to make invoke the login button when enter is typed
     * @param e
     */
    private void enterKeyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (usernameField.getText().isBlank() || Arrays.toString(passwordField.getPassword()).isBlank()) {
                credentialError.setVisible(true);
            } else {
                logging_in.setVisible(true);
                doLogin();
            }
        }
    }

    /**
     * handler for login button when clicked
     * @param e
     */
    private void loginMouseClicked(MouseEvent e) {
        if (usernameField.getText().isBlank() || Arrays.toString(passwordField.getPassword()).isBlank()) {
            credentialError.setVisible(true);
        } else {
            logging_in.setVisible(true);
            doLogin();
        }

    }

    /**
     * Handler to close the error window when clicked
     * @param e
     */
    private void credentialErrorMouseClicked(MouseEvent e) {
        credentialError.dispose();
    }

    /**
     * Handler for the create new account button
     * @param e
     */
    private void createNewAccountMouseClicked(MouseEvent e) {
        var sign_Up = new Create_New_Acc(client);
        sign_Up.setVisible(true);
    }

    /**
     * Initialises the JFrame components
     */
    private void initComponents() {
        JPanel backgroundPanel = new JPanel();
        JLabel label4 = new JLabel();
        JPanel loginPanel = new JPanel();
        JLabel label2 = new JLabel();
        JLabel label3 = new JLabel();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton login = new JButton();
        JButton button1 = new JButton();
        JLabel label5 = new JLabel();
        credentialError = new JWindow();
        JLabel label1 = new JLabel();
        logging_in = new JWindow();
        JLabel label8 = new JLabel();

        //======== this ========
        setTitle("Feather [Login]                                                                  ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== backgroundPanel ========
        {
            backgroundPanel.setBackground(Color.lightGray);
            backgroundPanel.setBorder(new CompoundBorder(new TitledBorder(new EmptyBorder(0, 0, 0,
                    0), "", TitledBorder.CENTER, TitledBorder.BOTTOM, new Font("", Font.BOLD,
                    12), Color.red), backgroundPanel.getBorder()));
            backgroundPanel
                    .addPropertyChangeListener(e -> {
                        if ("".equals(e.getPropertyName())) throw new RuntimeException();
                    });
            backgroundPanel.setLayout(null);

            //---- label4 ----
            label4.setIcon(new ImageIcon(Objects.requireNonNull(getClass()
                    .getResource("/client/pictures/feather.png"))));
            label4.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 12));
            label4.setBackground(Color.white);
            label4.setBorder(LineBorder.createBlackLineBorder());
            backgroundPanel.add(label4);
            label4.setBounds(175, 40, 75, 80);

            //======== loginPanel ========
            {
                loginPanel.setBackground(Color.white);
                loginPanel.setBorder(new LineBorder(Color.black, 2, true));
                loginPanel.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        enterKeyTyped(e);
                    }
                });
                loginPanel.setLayout(null);

                //---- label2 ----
                label2.setBackground(Color.green);
                label2.setIcon(new ImageIcon(Objects.requireNonNull(getClass()
                        .getResource("/client/pictures/icon.png"))));
                loginPanel.add(label2);
                label2.setBounds(35, 85, 40, 40);

                //---- label3 ----
                label3.setIcon(new ImageIcon(Objects.requireNonNull(getClass()
                        .getResource("/client/pictures/key.png"))));
                loginPanel.add(label3);
                label3.setBounds(35, 160, 35, 30);

                //---- usernameField ----
                usernameField.setBackground(Color.lightGray);
                usernameField.setBorder(new LineBorder(Color.black, 1, true));
                loginPanel.add(usernameField);
                usernameField.setBounds(95, 90, 190, 26);

                //---- passwordField ----
                passwordField.setBackground(Color.lightGray);
                passwordField.setBorder(new LineBorder(Color.black, 1, true));
                loginPanel.add(passwordField);
                passwordField.setBounds(95, 160, 190, 30);

                //---- login ----
                login.setIcon(new ImageIcon(Objects.requireNonNull(getClass()
                        .getResource("/client/pictures/login.png"))));
                login.setText("Log in");
                login.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 12));
                login.setBackground(Color.white);
                login.setBorder(null);
                login.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        loginMouseClicked(e);
                    }
                });
                loginPanel.add(login);
                login.setBounds(95, 235, 70, 40);

                //---- button1 ----
                button1.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 12));
                button1.setBorder(null);
                button1.setIcon(new ImageIcon(Objects.requireNonNull(getClass()
                        .getResource("/client/createIcons/signupppp.png"))));
                button1.setText("Sign up");
                button1.setBorderPainted(false);
                button1.setBackground(Color.white);
                button1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        createNewAccountMouseClicked(e);
                    }
                });
                loginPanel.add(button1);
                button1.setBounds(205, 240, 70, 35);

                //---- label5 ----
                label5.setText("Welcome to Feather");
                label5.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 12));
                label5.setHorizontalAlignment(SwingConstants.CENTER);
                loginPanel.add(label5);
                label5.setBounds(95, 40, 160, label5.getPreferredSize().height);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < loginPanel.getComponentCount(); i++) {
                        Rectangle bounds = loginPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = loginPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    loginPanel.setMinimumSize(preferredSize);
                    loginPanel.setPreferredSize(preferredSize);
                }
            }
            backgroundPanel.add(loginPanel);
            loginPanel.setBounds(40, 90, 340, 300);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < backgroundPanel.getComponentCount(); i++) {
                    Rectangle bounds = backgroundPanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = backgroundPanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                backgroundPanel.setMinimumSize(preferredSize);
                backgroundPanel.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(backgroundPanel);
        backgroundPanel.setBounds(0, 0, 410, 440);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < contentPane.getComponentCount(); i++) {
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
        pack();
        setLocationRelativeTo(getOwner());

        //======== credentialError ========
        {
            credentialError.setBackground(Color.red);
            credentialError.setOpacity(0.5F);
            credentialError.setForeground(Color.red);
            credentialError.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    credentialErrorMouseClicked(e);
                }
            });
            var credentialErrorContentPane = credentialError.getContentPane();
            credentialErrorContentPane.setLayout(null);

            //---- label1 ----
            label1.setText("Incorrect Username or Password");
            label1.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 14));
            label1.setBorder(new LineBorder(Color.black, 5, true));
            credentialErrorContentPane.add(label1);
            label1.setBounds(0, 0, 210, 35);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < credentialErrorContentPane.getComponentCount(); i++) {
                    Rectangle bounds = credentialErrorContentPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = credentialErrorContentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                credentialErrorContentPane.setMinimumSize(preferredSize);
                credentialErrorContentPane.setPreferredSize(preferredSize);
            }
            credentialError.pack();
            credentialError.setLocationRelativeTo(credentialError.getOwner());
        }

        //======== logging_in ========
        {
            logging_in.setOpacity(0.5F);
            var logging_inContentPane = logging_in.getContentPane();
            logging_inContentPane.setLayout(null);

            //---- label8 ----
            label8.setText("text");
            label8.setHorizontalAlignment(SwingConstants.CENTER);
            label8.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/pictures/7Tix.gif"))));
            label8.setOpaque(true);
            logging_inContentPane.add(label8);
            label8.setBounds(5, 5, 220, 145);
            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < logging_inContentPane.getComponentCount(); i++) {
                    Rectangle bounds = logging_inContentPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = logging_inContentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                logging_inContentPane.setMinimumSize(preferredSize);
                logging_inContentPane.setPreferredSize(preferredSize);
            }
            logging_in.pack();
            logging_in.setLocationRelativeTo(logging_in.getOwner());
        }
        this.setVisible(true);
    }

    /**
     * method to do the actual process of logging in
     * Communicates with the Chat Client to get confirmation
     */
    private void doLogin() {
        String login = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {
            if (client.login(login, password)) {
                // bring up the Chatroom
                this.dispose();
                new ChatroomUI(client,login);
            } else {
                // show error message
                credentialError.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * test drive (obsolete)
     * @param args

    public static void main(String[] args) {
        var hi = new Sign_In();
        hi.setVisible(true);
    }*/
}
