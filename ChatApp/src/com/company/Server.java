package com.company;


import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
        private JTextField userText;
        private JTextArea chatWindow;
        private ObjectOutputStream output;

        private ObjectInputStream input;
        private ServerSocket server;
        private Socket connection;


        //constructor
        public Server(){

            super("Instant Messenger");
            userText = new JTextField();
            userText.setEnabled(false);
            userText.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            sendMessage(event.getActionCommand());
                            userText.setText("");
                        }
                    }
            );
            add(userText, BorderLayout.NORTH);
            chatWindow = new JTextArea();
            add(new JScrollPane(chatWindow));
            setSize(300,150);
            setVisible(true);

        }
    //set up and run the server

    public void startRunning(){
            try{
                server = new ServerSocket(6789, 100);
                while(true){
                    try{

                        // connect and have conversation
                        waitForConnection();
                        setupStreams();
                        whileChatting();

                    }catch (EOFException eofException){
                        showMessage("\n Server ended connection!");
                    }finally {
                        closeCrap();
                    }
                }

            }catch (IOException ioException){
                ioException.printStackTrace();
            }
    }
    //wait for connection,
    // display info;
    private void waitForConnection() throws IOException{
            showMessage("waiting for someone to connect....");
            connection = server.accept();
            showMessage("Now connected to "+ connection.getInetAddress().getHostName());
    }

    // get stream to send and get info
    private  void setupStreams() throws IOException{

            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            showMessage("\n Streams are now setup\n");
    }

    // while chat conversation
    private void whileChatting() throws IOException{
            String message = " U R Connected";
            sendMessage(message);
            ableToType(true);
            do{
                // have a converstion
                try{
                    message = (String) input. readObject();
                    showMessage("\n"+ message);
                }catch(ClassNotFoundException classNotFoundException){
                    showMessage("\n idk wtf that send");
                }
            }while(!message.equals("CLIENT - END"));
    }

    // close steams and sockets after chatting
    private void closeCrap(){
            showMessage("\n Closing connection..... \n");
            ableToType(false);
            try{
                output.close();
                input.close();
                connection.close();
            }catch(IOException ioException){
                ioException.printStackTrace();
            }
    }

    // send mag to client
    private  void sendMessage(String message){
            try{

                output.writeObject("SERVER -" + message);
                output.flush();
                showMessage("\nSERVER -"+ message);

            }catch(IOException ioExpection){
                chatWindow.append("\n ERROR, CANNOT SEND MSG");
            }

    }
    //update chatWIndow
    private  void showMessage(final  String text) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );
    }

    // let user to type into box
    private void ableToType(final  boolean tof){
            SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );
    }


}
