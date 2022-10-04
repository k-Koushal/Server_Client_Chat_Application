package com.messengerapp.messenger_application;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader; // input Stream;
    private BufferedWriter bufferedWriter; // output Stream

    //Constructor:


    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
       try{
           this.socket=serverSocket.accept();
           this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.bufferedWriter= new BufferedWriter((new OutputStreamWriter(socket.getOutputStream())));
       }
       catch (IOException e){
           System.out.println("Error creating Server");
           e.printStackTrace();
           closeAll(socket,bufferedWriter,bufferedReader);
       }
    }

    // Method to send message to client:
    public  void sendMessageToClient(String messageToClient){
        try{
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to the client");
            closeAll(socket,bufferedWriter,bufferedReader);
        }
    }

    // Method to receive message from client:

    public void receiveMessageFromClient(VBox vbox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try{
                        String messageFromClient= bufferedReader.readLine();
                        messengerController.addLabel(messageFromClient,vbox);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        System.out.println(" Error receiving message from client");
                        closeAll(socket,bufferedWriter,bufferedReader);
                        break;
                    }
                }
            }
        }).start();
    }

    // method to close all streams:
    public static void closeAll(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        try{
            if(socket!=null){
                socket.close();
            }
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
/*
Socket:
    communication between a Server machine and Client machine happens through Sockets.
    each client has a unique socket number with respect to the server machine;
    so N-number of clients ---> requires --> N-different Sockets.
    Java socket does this work by creating a new socket everytime a new client sends communication request.
    all it needs is Port_Number && Client's IP address;

    Note: a Socket has an Input & output Stream;
 */