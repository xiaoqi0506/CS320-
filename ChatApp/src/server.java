import java.io.*;
import java.net.*;

public class server {
    public static void main ( String [] args) throws IOException{
        ServerSocket ss= new ServerSocket(10000) ;
        System.out.println("Server is running...");


        Socket s = ss.accept();

        System.out.println("Client Connected");

        InputStreamReader in= new InputStreamReader(s.getInputStream());
        BufferedReader bf =new BufferedReader(in);

        String str = bf. readLine();
        System.out.println("client :" + str );

        PrintWriter pr= new PrintWriter(s.getOutputStream());
        pr.println("yes");
        pr.flush();



    }


}