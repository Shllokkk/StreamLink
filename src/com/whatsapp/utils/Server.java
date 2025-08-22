/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.whatsapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Owner
 */
public class Server {
    private static HashMap<String, Socket> clientMap = new HashMap<>();

    public static HashMap<String, Socket> getMap() {
        return clientMap;
    }
    
    public static Set<String> getUsernameSet() {
        return clientMap.keySet();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(8761);
        System.out.println("Server is waiting for a connection on port: " + server.getLocalPort());

        while(true) {
            String clientUsername = "";

            Socket clientSocket = server.accept();
            // this is a blocking method which is listening in loop for a connection to get the username for clientSocket
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientUsername = br.readLine();
  
            clientMap.put(clientUsername, clientSocket); 
            System.out.println(clientMap);
            new ClientHandler(clientUsername, clientSocket);
        }
    }
}
