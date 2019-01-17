package com.tandilserver.poker_room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tandilserver.poker_intercom.customTypes.LimitTypes;
import com.tandilserver.poker_intercom.customTypes.ServerTypes;
import com.tandilserver.poker_intercom.handshake.ServerInfo;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	InetAddress address=InetAddress.getLocalHost();
        Socket s1=null;
        String line=null;
        BufferedReader br=null;
        BufferedReader is=null;
        PrintWriter os=null;

        try {
            s1=new Socket(address, 9091); // You can use static final constant PORT_NUM
            br= new BufferedReader(new InputStreamReader(System.in));
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : "+address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        String response=null;
        try{
            line=br.readLine(); 
            while(line.compareTo("QUIT")!=0){
            		ServerInfo srvInfo = new ServerInfo();
            		srvInfo.blind = 50;
            		srvInfo.ip = "127.0.0.1";
            		srvInfo.limit_bet = LimitTypes.NO_LIMIT;
            		srvInfo.min_bet = 50;
            		srvInfo.players = 0;
            		srvInfo.port = 6666;
            		srvInfo.server_name = "TST";
            		srvInfo.server_type = ServerTypes.SIT_N_GO;
            		ObjectMapper oM = new ObjectMapper();
                    os.println(oM.writeValueAsString(srvInfo));
                    os.flush();
                    response=is.readLine();
                    System.out.println("Server Response : "+response);
                    line=br.readLine();

                }
        }
        catch(IOException e){
            e.printStackTrace();
        System.out.println("Socket read Error");
        }
        finally{

            is.close();
            os.close();
            br.close();
            s1.close();
            System.out.println("Connection Closed");

        }
    }
}
