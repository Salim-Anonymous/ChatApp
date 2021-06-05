package client.logging_in_Frames;

import chatroom.ChatroomUI;
import client.ChatClient2_0;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

/**
 * new JFrame to get the new account details from the user
 * reminder:
 * ============================
 * verification of the details has still not been configured yet
 * may need to work on getting the verifications on the server and the database
 * @author Salim
 */
public class Create_New_Acc extends JFrame {
    ChatClient2_0 client;
    private JTextField phone;
    private JTextField usernameField;
    private JTextField studentID;
    private JTextField email_address;
    private JTextField departmentField;
    private JTextArea address;
    private JPasswordField passwordField;

    /**
     * just the usual constructor
     * @param client
     */
    public Create_New_Acc(ChatClient2_0 client) {
        initComponents();
        this.client = client;
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Event handler when submit button is clicked
     * @param e
     */
    private void submitButtonMouseClicked(MouseEvent e) {
        ///database work // TODO: ༢༠༢༡-༠༤-༢༠

        String[] details = {phone.getText(),email_address.getText(),
                passwordField.getText(),usernameField.getText(),
                studentID.getText(),departmentField.getText(),address.getText()};
        try {
            client.createNewAccount(details);
            System.out.println("User Creation Successful: Logging in");
            if(client.login(details[4],details[2])){
                new ChatroomUI(client,details[4]);
                this.dispose();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * initialize all the UI
     */
    private void initComponents() {

        JLabel logo = new JLabel();
        JPanel detailsPanel = new JPanel();
        JLabel userIcon = new JLabel();
        usernameField = new JTextField();
        phone = new JTextField();
        JLabel passIcon = new JLabel();
        JLabel mailIcon = new JLabel();
        email_address = new JTextField();
        JLabel slogan = new JLabel();
        JLabel department = new JLabel();
        departmentField = new JTextField();
        JButton submitButton = new JButton();
        JLabel locationIcon = new JLabel();
        JScrollPane scrollPane1 = new JScrollPane();
        address = new JTextArea();
        passwordField = new JPasswordField();
        JPasswordField confirmPassword = new JPasswordField();
        JPanel backgroundPanel = new JPanel();
        studentID = new JTextField();

        //======== this ========
        setTitle("Feather [Create New Account]");
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- logo ----
        logo.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/pictures/feather.png"))));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setBorder(new LineBorder(Color.black, 2, true));
        contentPane.add(logo);
        logo.setBounds(190, 30, 75, 80);

        //======== detailsPanel ========
        {
            detailsPanel.setBackground(Color.white);
            detailsPanel.setBorder(new LineBorder(Color.black, 2, true));
            detailsPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new
                    javax.swing.border.EmptyBorder(0, 0, 0, 0), "", javax
                    .swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java
                    .awt.Font("", java.awt.Font.BOLD, 12), java.awt
                    .Color.red), detailsPanel.getBorder()));
            detailsPanel.addPropertyChangeListener(e -> {
                if ("".
                        equals(e.getPropertyName())) throw new RuntimeException();
            });
            detailsPanel.setLayout(null);

            //---- userIcon ----
            userIcon.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/pictures/icon.png"))));
            userIcon.setHorizontalAlignment(SwingConstants.CENTER);
            detailsPanel.add(userIcon);
            userIcon.setBounds(10, 80, 45, 40);

            //---- usernameField ----
            usernameField.setToolTipText("Enter Username");
            usernameField.setBorder(new LineBorder(Color.black, 1, true));
            detailsPanel.add(usernameField);
            usernameField.setBounds(75, 85, 265, usernameField.getPreferredSize().height);

            //------Student ID---------------
            studentID.setToolTipText("Enter student ID");
            studentID.setBorder(new LineBorder(Color.black,1,true));
            detailsPanel.add(studentID);
            studentID.setBounds(75,115,250,20);

            //---- passIcon ----
            passIcon.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/pictures/key.png"))));
            detailsPanel.add(passIcon);
            passIcon.setBounds(15, 145, 35, 35);

            //---- mailIcon ----
            mailIcon.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/createIcons/email.png"))));
            detailsPanel.add(mailIcon);
            mailIcon.setBounds(new Rectangle(new Point(20, 235), mailIcon.getPreferredSize()));

            //---- email address ----
            email_address.setToolTipText("Enter Email Address");
            email_address.setBorder(new LineBorder(Color.black, 1, true));
            detailsPanel.add(email_address);
            email_address.setBounds(75, 225, 285, 25);

            //---- slogan ----
            slogan.setText("One For All");
            slogan.setHorizontalAlignment(SwingConstants.CENTER);
            slogan.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 16));
            detailsPanel.add(slogan);
            slogan.setBounds(130, 45, 140, slogan.getPreferredSize().height);

            //---- department ----
            department.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/createIcons/dept.jpg"))));
            detailsPanel.add(department);
            department.setBounds(new Rectangle(new Point(20, 290), department.getPreferredSize()));

            //---- departmentField ----
            departmentField.setBorder(new LineBorder(Color.black, 1, true));
            departmentField.setToolTipText("Enter Department Name");
            detailsPanel.add(departmentField);
            departmentField.setBounds(75, 295, 285, 25);

            //----phone number-------------//
            phone.setToolTipText("Enter the phone number");
            phone.setBorder(new LineBorder(Color.black, 1, true));
            detailsPanel.add(phone);
            phone.setBounds(75, 255, 285, 25);

            //---- submitButton ----
            submitButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/createIcons/submit.png"))));
            submitButton.setHorizontalAlignment(SwingConstants.LEFT);
            submitButton.setText("SUBMIT");
            submitButton.setBorder(new LineBorder(Color.black, 2, true));
            submitButton.setFont(new Font("Yrsa", Font.BOLD | Font.ITALIC, 12));
            submitButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    submitButtonMouseClicked(e);
                }
            });
            detailsPanel.add(submitButton);
            submitButton.setBounds(260, 355, 90, 30);

            //---- locationIcon ----
            locationIcon.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/client/createIcons/address.jpg"))));
            detailsPanel.add(locationIcon);
            locationIcon.setBounds(new Rectangle(new Point(20, 340), locationIcon.getPreferredSize()));

            //======== scrollPane1 ========
            {

                //---- address ----
                address.setBorder(new LineBorder(Color.black, 1, true));
                scrollPane1.setViewportView(address);
            }
            detailsPanel.add(scrollPane1);
            scrollPane1.setBounds(75, 340, 155, 65);

            //---- passwordField ----
            passwordField.setToolTipText("Enter Password");
            passwordField.setBorder(new LineBorder(Color.black, 1, true));
            detailsPanel.add(passwordField);
            passwordField.setBounds(75, 140, 215, 25);

            //---- confirmPassword ----
            confirmPassword.setToolTipText("Confirm Password");
            confirmPassword.setBorder(new LineBorder(Color.black, 1, true));
            detailsPanel.add(confirmPassword);
            confirmPassword.setBounds(75, 175, 215, 25);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < detailsPanel.getComponentCount(); i++) {
                    Rectangle bounds = detailsPanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = detailsPanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                detailsPanel.setMinimumSize(preferredSize);
                detailsPanel.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(detailsPanel);
        detailsPanel.setBounds(25, 65, 425, 435);

        //======== backgroundPanel ========
        {
            backgroundPanel.setBackground(Color.lightGray);
            backgroundPanel.setLayout(null);

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
        backgroundPanel.setBounds(0, -5, 480, 550);

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

    }



}


