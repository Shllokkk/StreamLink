/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.whatsapp.utils;

import com.whatsapp.main.Dashboard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Owner
 */
public class Client {
    private static Socket socket;
    private static String clientUsername = "";
    private static String inputFromStream = "";
    private static String message = "";
    private static String xmlMessage = "";
    private static String activeUserString = "";
    private static PrintWriter pwClientOutputStream;
    private static BufferedReader brClientInputStream;
    private static Set<String> activeUserSet;
   
    private static Thread receiveMessageThread;
    private static Thread endConnectionThread;
    
    public static void getConnection(String usernameFromDash) {
        try {
            clientUsername = usernameFromDash;
            socket = new Socket("127.0.0.1", 8761);
            
            brClientInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pwClientOutputStream = new PrintWriter(socket.getOutputStream(), true);
            
            pwClientOutputStream.println(clientUsername);
            System.out.println(clientUsername + " connected!   Port: " + socket.getLocalPort());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void sendMessageToStream(String type, String receiverUsername, String message)throws IOException {
        // the main thread handles sending messages
            xmlMessage = XMLParser.toXML(type, receiverUsername, clientUsername, message);
            pwClientOutputStream.println(xmlMessage);
    }
    
    public static void receiveInputFromStream(Dashboard dashboard) {
        // thread to receive input from stream and seperating on the basis of type simultaneously
        receiveMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while((inputFromStream = brClientInputStream.readLine()) != null)
                        if(inputFromStream.startsWith("MESSAGE@")) {
                            message = inputFromStream.substring(8);
                            dashboard.receiveMessage(message);
                        } else if(inputFromStream.startsWith("USERS@")) {
                            activeUserString = inputFromStream.substring(6);
                            activeUserSet = new HashSet<>(Arrays.asList(activeUserString.split(",")));
                            dashboard.updateActiveUsers(activeUserSet);
                        }
                } catch (IOException e) {
                } // we catch the exception but do nothing
            }
        });
        receiveMessageThread.start();
    }
    
    public static void endConnection() throws InterruptedException{
        endConnectionThread = new Thread(() -> {
            try {
//                if(brClientInputStream != null) {
//                    System.out.println("closing input");
//                    brClientInputStream.close(); 
//                }
//                if(pwClientOutputStream != null) {
//                    System.out.println("closing output");
//                    pwClientOutputStream.close();
//
//                }
                socket.close();  // as readline() is a blocking method we force readline() on client handler-side to throw an exception by closing the socket and thus killing the thread
                System.out.println(clientUsername + " exited!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endConnectionThread.start();
    }
}
