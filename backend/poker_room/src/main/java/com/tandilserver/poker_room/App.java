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
import com.tandilserver.poker_intercom.handshake.AuthorizationBearer;
import com.tandilserver.poker_intercom.handshake.ServerInfo;

public class App 
{
	public final static int PORT = 9091;
	
    public static void main( String[] args ) throws IOException {
    	InetAddress address=InetAddress.getLocalHost();
        Socket s1=null;
        String line=null;
        BufferedReader br=null;
        BufferedReader is=null;
        PrintWriter os=null;
        try {
            s1=new Socket(address, PORT); // You can use static final constant PORT_NUM
            br= new BufferedReader(new InputStreamReader(System.in));
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
        
        String response=null;
        try{
	        System.out.println(">--{ Client Address : "+address);
	        System.out.println(">--[ Configuración del servidor ]--<");
	        System.out.println("------------------------------------");
	        System.out.println("[?] Server name: ");
	        String serverName = br.readLine();
	        System.out.println("[?] Server type: \r\n");
	        System.out.println("0 - Sit & Go <AutoSelected>");
	        System.out.println("[?] Big Blind: ");
	        int blind = Integer.parseInt(br.readLine());
	        System.out.println("[?] Minimum bet: ");
	        int min_bet = Integer.parseInt(br.readLine());
	        System.out.println("[?] Limit bet: \r\n");
	        System.out.println("0 - NO LIMIT");
	        System.out.println("------------------------------------");
	        System.out.println("[*] Server name: "+serverName);
	        System.out.println("[*] Server Type: Sit&Go");
	        System.out.println("[*] Big Blind: " + blind);
	        System.out.println("[*] Short Blind: "+blind/2);
	        System.out.println("[*] Minimum Bet: "+min_bet);
	        System.out.println("[*] Limit bet: NO LIMIT");
	        System.out.println("[>] Loggin room: ");
	        ServerInfo srvInfo = new ServerInfo();
	        srvInfo.ip = address.getHostAddress();
	        srvInfo.server_type = ServerTypes.SIT_N_GO;
	        srvInfo.server_name = serverName;
	        srvInfo.blind = blind;
	        srvInfo.min_bet = min_bet;
	        srvInfo.limit_bet = LimitTypes.NO_LIMIT;
	        srvInfo.players = 0;
	        srvInfo.port = PORT;
	        ObjectMapper oM = new ObjectMapper();
	        os.println(oM.writeValueAsString(srvInfo));
	        os.flush();
	        System.out.println("[!] Waiting for response...");
	        response = is.readLine();
	        System.out.println("[<] Response: " + response);
	        AuthorizationBearer auth = oM.readValue(response, AuthorizationBearer.class);
	        if (auth.valid) {
	        	authorized(auth, br);
	        } else {
	        	System.out.println("[!] Authorization required, plesae write the last Server Identity Hash:");
	        	String ServerIdentityHash = br.readLine();
	        	auth.server_identity_hash = ServerIdentityHash;
	        	System.out.println("[>] Sending Server Identity Hash...");
	        	os.println(oM.writeValueAsString(auth));
	        	System.out.println("[!] Waiting for response...");
	        	response = is.readLine();
	        	System.out.println("[<] Response: " + response);
	        	auth = oM.readValue(response, AuthorizationBearer.class);
	        	if(auth.valid) {
	        		authorized(auth, br);
	        	} else {
	        		System.out.println("[!] Authorization rejected.");
	        	}
	        }
	        
	        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        } finally{
            is.close();
            os.close();
            br.close();
            s1.close();
            System.out.println("Connection Closed");

        }
    }
    
    public static void authorized(AuthorizationBearer auth, BufferedReader console) throws IOException {
    	System.out.println("[*] Access Garanted");
    	System.out.println("[*] New Server Identity Hash: "+auth.server_identity_hash);
    	System.out.println("!!! Please save this identity for next access");
    	System.out.println("--------------------------");
    	System.out.println("Press any key to close");
    	console.read();
    }
}
