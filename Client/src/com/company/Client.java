package com.company;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class Client extends JFrame {
    private JTextField userTest;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    //constructor
    public Client(String host) {
        super("Client mofo");
        serverIP = host;
        userTest = new JTextField();
        userTest.setEditable(false);
        userTest.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        sendMessage(event.getActionCommand());
                        userTest.setText("");
                    }
                }
        );
        add(userTest, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }

    // connect to server
    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();


        } catch (EOFException eofException) {
            showMessage("\n Client termineated connection");
        } catch (IOException ioEception) {
            ioEception.printStackTrace();
        } finally {
            closeCrap();

        }
    }

    // connect to server
    private void connectToServer() throws IOException {
        showMessage("Attempting connection.....\n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("connected to " + connection.getInetAddress().getHostName());

    }

    // set up stream to send and recerive
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Stream ready to go!\n");
    }

    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("\n UNKNONW OBJ TYPE");
            }
        } while (!message.equals("SERVER - END"));
    }

    // close the stream and socke
    private void closeCrap() {
        showMessage("\n closing crap down....");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // send Mesg to server
    private void sendMessage(String message) {
        try {
            output.writeObject("CLIENT -" + message);
            output.flush();
            showMessage("\n CLIENT -" + message);

        } catch (IOException ioException) {
            chatWindow.append("\n someting mess up sending message hoss");
        }
    }

    // change updat chatwindow
    private void showMessage(final String message) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(message);
                    }
                }
        );
    }

    //type to the chat box
    private void ableToType(final Boolean tof) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userTest.setEditable(tof);
                    }
                }
        );
    }
}