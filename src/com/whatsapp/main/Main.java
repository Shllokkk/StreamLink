/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.whatsapp.main;

import com.whatsapp.utils.MySQLConnector;
import com.whatsapp.components.PanelCover;
import com.whatsapp.components.PanelLoading;
import com.whatsapp.components.PanelLoginAndRegister;
import com.whatsapp.components.PanelVerifyCode;
import com.whatsapp.components.PanelPopupMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import javax.swing.JLayeredPane;
import javax.swing.Timer;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author Owner
 */
public class Main extends javax.swing.JFrame {

    private MigLayout layout;
    private PanelCover cover;
    private PanelLoginAndRegister loginAndRegister;
    private PanelLoading loading;
    private PanelVerifyCode verifyCode;
    
    private final double addSize = 30;
    private final double coverSize = 40;
    private final double loginSize = 60;
    
    private boolean isLogin;
    private final DecimalFormat df = new DecimalFormat("##0.###");
    
    Connection connection;
    
    public Main() {
        connection = MySQLConnector.getConnection();
        initComponents();
        init();
    }
    
    private void init() {
        layout = new MigLayout("fill, insets 0");
        cover = new PanelCover();
        loading = new PanelLoading();
        loginAndRegister = new PanelLoginAndRegister(this, connection);
        verifyCode = new PanelVerifyCode(this, loginAndRegister);

        
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double fractionCover;
                double fractionLogin;
                double size = coverSize;
                
                if(fraction <= 0.5f) {
                    size += fraction * addSize;
                } else {
                    size += addSize - fraction * addSize;
                }
                
                if(isLogin) {
                    fractionCover = 1f - fraction;
                    fractionLogin =fraction;
                    
                    if(fraction >= 0.5f) {
                        cover.registerRight(fractionCover * 100);
                    } else {
                        cover.loginRight(fractionLogin * 100);
                    }
                } else {
                    fractionCover = fraction;
                    fractionLogin = 1f - fraction;
                    
                    if(fraction <= 0.5f) {
                        cover.registerLeft(fraction * 100);
                    } else {
                        cover.loginLeft((1f - fraction) * 100);
                    }
                }
                
                if(fraction >= 0.5f) {
                    loginAndRegister.showRegister(isLogin);
                }
                fractionCover = Double.parseDouble(df.format(fractionCover));
                fractionLogin = Double.parseDouble(df.format(fractionLogin));
                
                layout.setComponentConstraints(cover, "width " + size + "%, pos " + fractionCover + "al 0 n 100%");
                layout.setComponentConstraints(loginAndRegister, "width " + loginSize + "%, pos " + fractionLogin + "al 0 n 100%");
                jLyrPnBg.revalidate();
            }

            @Override
            public void end() {
                isLogin = !isLogin;
            }
        };
        Animator animator = new Animator(800, target);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.setResolution(0);    // for smooth animation
        
        jLyrPnBg.setLayout(layout);
        jLyrPnBg.setLayer(loading, JLayeredPane.POPUP_LAYER);
        jLyrPnBg.setLayer(verifyCode, JLayeredPane.POPUP_LAYER);
        jLyrPnBg.add(loading, "pos 0 0 100% 100%");
        jLyrPnBg.add(verifyCode, "pos 0 0 100% 100%");
        jLyrPnBg.add(cover, "width " + coverSize + "%, pos 0al 0 n 100%");
        jLyrPnBg.add(loginAndRegister,  "width " + loginSize + "%, pos 1al 0 n 100%");
        verifyCode.setVisible(false);
        
        cover.addEvent(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                if(!animator.isRunning()) {
                    animator.start();
                }
            }
        });
    } 
    
    public void showMessage(PanelPopupMessage.MessageType messageType, String message) {
        PanelPopupMessage ms = new PanelPopupMessage();
        ms.showMessage(messageType, message);
        ms.repaint();
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                if (!ms.getShow()) {
                    jLyrPnBg.add(ms, "pos 0.5al -30", 0); //  insert to jLyrPnBg fist index 0
                    ms.setVisible(true);
                    jLyrPnBg.revalidate();
                    jLyrPnBg.repaint();   
                }
            }

            @Override
            public void timingEvent(float fraction) {
                float f;
                if (ms.getShow()) {
                    f = 40 * (1f - fraction);
                } else {
                    f = 40 * fraction;
                }
                layout.setComponentConstraints(ms, "pos 0.5al " + (int) (f - 30));
                jLyrPnBg.revalidate();
                jLyrPnBg.repaint();
                }

            @Override
            public void end() {   // not working as expeceted
                if (ms.getShow()) {
                    ms.setVisible(false);
                    jLyrPnBg.remove(ms);
                    jLyrPnBg.revalidate(); 
                    jLyrPnBg.repaint(); 
                    ms.setShow(false); 
                } else {
                    ms.setShow(true);
                }
}
        };
        Animator animator = new Animator(300, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.start();
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ms.setVisible(false);
                ms.setShow(false);
                jLyrPnBg.remove(ms);
                jLyrPnBg.revalidate(); 
                jLyrPnBg.repaint(); 
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public void showLoadingPanel() {
        loading.setVisible(true);
        jLyrPnBg.revalidate();
        jLyrPnBg.repaint();
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loading.setVisible(false);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public void showVerifyCodePanel(String username, String email, String password) {
        verifyCode.setupMailService(username, email, password);
        verifyCode.setVisible(true);
        jLyrPnBg.revalidate();
        jLyrPnBg.repaint();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLyrPnBg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLyrPnBg.setBackground(new java.awt.Color(255, 255, 255));
        jLyrPnBg.setOpaque(true);

        javax.swing.GroupLayout jLyrPnBgLayout = new javax.swing.GroupLayout(jLyrPnBg);
        jLyrPnBg.setLayout(jLyrPnBgLayout);
        jLyrPnBgLayout.setHorizontalGroup(
            jLyrPnBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
        );
        jLyrPnBgLayout.setVerticalGroup(
            jLyrPnBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLyrPnBg)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLyrPnBg)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane jLyrPnBg;
    // End of variables declaration//GEN-END:variables
}
