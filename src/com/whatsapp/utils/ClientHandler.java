/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.whatsapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Owner
 */
public class ClientHandler extends Thread{
    private String clientUsername = "";
    private String receiverUsername = "";
    private Socket clientSocket;
    private Socket receiverSocket;
    private String message = "";
    private String xmlMessage = "";
    private String[] xmlValuesArray;
    private static HashMap<String, Socket> clientMap;
    private Set<String> clientUsernameSet;

    ClientHandler(String username, Socket socket) {
        clientMap = Server.getMap();
        clientUsernameSet = Server.getUsernameSet();
        this.clientUsername = username;
        this.clientSocket = socket;
        System.out.println( clientUsername + " Connected!");
        start();
    }

    @Override
    public void run() {
        try{
            broadcastActiveUsers();
            BufferedReader brSenderInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter pwSenderOutputStream = new PrintWriter(clientSocket.getOutputStream(), true);
            while(true) {
                xmlMessage = brSenderInputStream.readLine();  // this throws null pointer exception after stream closes
                xmlValuesArray = XMLParser.parseXML(xmlMessage);  
                
                if("MESSAGE".equals(xmlValuesArray[0])) {
                    receiverSocket = clientMap.get(xmlValuesArray[1]);
                    receiverUsername = xmlValuesArray[1];
                    message = xmlValuesArray[3];
                    
                    if(receiverSocket != null) {
                        System.out.println("From(" + clientUsername + ") To(" + receiverUsername + "): " + message);
                        PrintWriter pwReceiverOutputStream = new PrintWriter(receiverSocket.getOutputStream(), true);
                        pwReceiverOutputStream.println("MESSAGE@" + xmlMessage);
                    } else {
                        System.out.println("Receiver Not Found-----From(" + clientUsername + ") To(" + receiverUsername + "): " + message);
                    }      
                }
            }           
        } catch(IOException | NullPointerException e) {
            System.out.println(clientUsername + " exited!");
        } finally {
            clientMap.remove(clientUsername);
            broadcastActiveUsers();
            try {
                clientSocket.close();
//                if(receiverSocket != null) 
//                    receiverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static void broadcastActiveUsers() {
        String activeUsers = "USERS@" + String.join(",", clientMap.keySet());

        for (Socket socket : clientMap.values()) {
            try {
                PrintWriter socketOutputStream = new PrintWriter(socket.getOutputStream(), true);
                socketOutputStream.println(activeUsers);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
