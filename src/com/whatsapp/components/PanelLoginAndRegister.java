/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.whatsapp.components;

import com.whatsapp.main.Dashboard;
import com.whatsapp.main.Main;
import com.whatsapp.swing.Button;
import com.whatsapp.swing.MyPasswordField;
import com.whatsapp.swing.MyTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import net.coobird.thumbnailator.Thumbnails;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Owner
 */
public class PanelLoginAndRegister extends javax.swing.JLayeredPane {
    Main mainObject;
    Connection connection;
    
    public static String superUsername;
    
    MyTextField tfSignUpUsername;
    MyTextField tfSignUpEmail;
    MyPasswordField tfSignUpPassword;

    MyTextField tfLoginUsername;
    MyPasswordField tfLoginPassword;
            
    public PanelLoginAndRegister(Main object, Connection connection) {
        initComponents();
        initRegisterPanel();
        initLoginPanel();
        this.mainObject = object;
        this.connection = connection;
        PnlLogin.setVisible(false);
        PnlRegister.setVisible(true);
    }
    
    private void initRegisterPanel() {
        PnlRegister.setLayout(new MigLayout("wrap" ,"push[center]push", "push[]30[]15[]15[]30[]push"));
        JLabel lblCreateAcc = new JLabel("Create Account");
        lblCreateAcc.setFont(new Font("san serif", 1, 30));
        lblCreateAcc.setForeground(new Color(7, 164, 121));
        PnlRegister.add(lblCreateAcc);
                
        tfSignUpUsername = new MyTextField();
        tfSignUpUsername.setPrefixIcon(new ImageIcon(getClass().getResource("/com/whatsapp/icons/user.png")));
        tfSignUpUsername.setHint("Username");
        PnlRegister.add(tfSignUpUsername, "w 60%");

        tfSignUpEmail = new MyTextField();
        tfSignUpEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/whatsapp/icons/mail.png")));
        tfSignUpEmail.setHint("Email");
        PnlRegister.add(tfSignUpEmail, "w 60%");
        
        tfSignUpPassword = new MyPasswordField();
        tfSignUpPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/com/whatsapp/icons/pass.png")));
        tfSignUpPassword.setHint("Password");
        PnlRegister.add(tfSignUpPassword, "w 60%"); 
        
        Button btnRegister = new Button();
        btnRegister.setBackground(new Color(7, 164, 121));
        btnRegister.setForeground(new Color(250, 250, 250));
        btnRegister.setText("SIGN UP");
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSignUp();
            }
        });
        PnlRegister.add(btnRegister, "w 40%, h 40");
    }
     
    private void initLoginPanel() {
        PnlLogin.setLayout(new MigLayout("wrap" ,"push[center]push", "push[]30[]15[]15[]30[]push"));
        JLabel lblSignIn = new JLabel("Sign In");
        lblSignIn.setFont(new Font("san serif", 1, 30));
        lblSignIn.setForeground(new Color(7, 164, 121));
        PnlLogin.add(lblSignIn);
                
        tfLoginUsername = new MyTextField();
        tfLoginUsername.setPrefixIcon(new ImageIcon(getClass().getResource("/com/whatsapp/icons/user.png")));
        tfLoginUsername.setHint("Username");
        PnlLogin.add(tfLoginUsername, "w 60%");
        
        tfLoginPassword = new MyPasswordField();
        tfLoginPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/com/whatsapp/icons/pass.png")));
        tfLoginPassword.setHint("Password");
        PnlLogin.add(tfLoginPassword, "w 60%"); 
        
        JButton btnForgotPass = new JButton("Forgot your password ?");
        btnForgotPass.setForeground(new Color(100, 100, 100));
        btnForgotPass.setFont(new Font("sansserif", 1, 12));
        btnForgotPass.setContentAreaFilled(false);
        btnForgotPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        PnlLogin.add(btnForgotPass);

        Button btnLogin = new Button();
        btnLogin.setBackground(new Color(7, 164, 121));
        btnLogin.setForeground(new Color(250, 250, 250));
        btnLogin.setText("SIGN IN");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processLogin();
            }
        });
        PnlLogin.add(btnLogin, "w 40%, h 40");
    }
    
    public void showRegister(Boolean show) {
        if(show) {
            PnlRegister.setVisible(true);
            PnlLogin.setVisible(false);
        } else {
            PnlRegister.setVisible(false);
            PnlLogin.setVisible(true);
        }
    }
    
    private void processSignUp() {
        Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9]{3,}$");
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])[A-Za-z0-9]{8,}$");
        
        if(!tfSignUpUsername.getText().isEmpty() && !tfSignUpEmail.getText().isEmpty() && !tfSignUpPassword.getText().isEmpty()) {
            String username = tfSignUpUsername.getText();
            String email = tfSignUpEmail.getText();
            String password = tfSignUpPassword.getText();
            
            Matcher usernameMatcher = usernamePattern.matcher(username);
            Matcher emailMatcher = emailPattern.matcher(email);
            Matcher passwordMatcher = passwordPattern.matcher(password);
            
            if(!usernameMatcher.matches()) {
                mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, "Username must have atleast 3 characters");
            } else if(!emailMatcher.matches()) {
                mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, " Invalid Email!");                
            }else if(!passwordMatcher.matches()) {
                mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, "Password must be at least 8 characters with 1 uppercase");                
            } else {
                validateSignUp(username, email, password);
            }
        } else {
            mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, "One or more fields empty!");
        }
    }
    
    private void processLogin() {
        String username = tfLoginUsername.getText();
        String password = tfLoginPassword.getText();
        
        String sql = "SELECT * FROM users WHERE BINARY Username = ? AND BINARY Password = ?";
        PreparedStatement ps;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                if(rs.getInt("Logged_In") == 1) {
                    mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, "User is already logged in a session");
                } else {
                    String sqlUpdateLogInStatus = "UPDATE users SET Logged_In = 1 WHERE Username = ?";
                    PreparedStatement ps1  = connection.prepareStatement(sqlUpdateLogInStatus);
                    ps1.setString(1, username);
                   
                    superUsername = rs.getString("Username");
                    mainObject.showMessage(PanelPopupMessage.MessageType.SUCCESS, "Log In successful!");
                    mainObject.showLoadingPanel();
                    ps1.execute(); // the logged_in status will be updated while the loading panel runs
                    
                    Timer timer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mainObject.dispose();
                            new Dashboard(superUsername).setVisible(true);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            } else {
                mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, "Invalid credentials!");
            }
        } catch(SQLException ex) {
            mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, ex.getMessage());
        }
    }
    
    private void validateSignUp(String username, String email, String password) {
        try {
            String sqlUsername = "SELECT * FROM users where Username = ?";
            String sqlEmail = "SELECT * FROM users where Email = ?";

            PreparedStatement ps1 = connection.prepareStatement(sqlUsername);
            PreparedStatement ps2 = connection.prepareStatement(sqlEmail);
            ps1.setString(1, username);
            ps2.setString(1, email);
            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            
            if(rs1.next())
                mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, "username already exists!");
            else if(rs2.next())
                mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, "email already exists!");
            else {
                mainObject.showLoadingPanel();
                Timer timer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainObject.showVerifyCodePanel(username, email, password);

                    }
                }) ; // shows verify panel after loading panel 
                timer.setRepeats(false);
                timer.start();
                // rest of account creation will be handled by the verification panel 
            }
        } catch(SQLException ex){
            mainObject.showMessage(PanelPopupMessage.MessageType.ERROR, ex.getMessage());
        }
    }  
    
    public void createAccount(String username, String email, String password) throws IOException, SQLException{
        String defaultImageFile = "C:\\Users\\Owner\\Desktop\\Whatsapp\\src\\com\\whatsapp\\icons\\user.png";
        byte[] defaultImage;
        BufferedImage img = Thumbnails.of(defaultImageFile).size(162, 164).asBufferedImage();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        defaultImage = baos.toByteArray();

        LocalDate date = LocalDate.now();
        Date accountCreationDateTime = Date.valueOf(date);

        String sql = "INSERT INTO users "
                + "(Username, Email, Password,Joining_date, Image) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, accountCreationDateTime.toString());
        ps.setBytes(5, defaultImage);

        ps.execute();
        mainObject.showMessage(PanelPopupMessage.MessageType.SUCCESS, "Account created Successfully!");
        tfSignUpUsername.setText("");
        tfSignUpEmail.setText("");
        tfSignUpPassword.setText("");
        mainObject.showLoadingPanel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlLogin = new javax.swing.JPanel();
        PnlRegister = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        PnlLogin.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout PnlLoginLayout = new javax.swing.GroupLayout(PnlLogin);
        PnlLogin.setLayout(PnlLoginLayout);
        PnlLoginLayout.setHorizontalGroup(
            PnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        PnlLoginLayout.setVerticalGroup(
            PnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(PnlLogin, "card3");

        PnlRegister.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout PnlRegisterLayout = new javax.swing.GroupLayout(PnlRegister);
        PnlRegister.setLayout(PnlRegisterLayout);
        PnlRegisterLayout.setHorizontalGroup(
            PnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        PnlRegisterLayout.setVerticalGroup(
            PnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(PnlRegister, "card2");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlLogin;
    private javax.swing.JPanel PnlRegister;
    // End of variables declaration//GEN-END:variables
}
