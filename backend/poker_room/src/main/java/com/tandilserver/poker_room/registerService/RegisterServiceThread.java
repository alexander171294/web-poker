package com.tandilserver.poker_room.registerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tandilserver.poker_intercom.customTypes.LimitTypes;
import com.tandilserver.poker_intercom.customTypes.ServerTypes;
import com.tandilserver.poker_intercom.handshake.AuthorizationBearer;
import com.tandilserver.poker_intercom.handshake.ServerInfo;

@Component
@PropertySources(value = {@PropertySource("classpath:roomRegisterService.properties")})
public class RegisterServiceThread  implements Runnable, ApplicationListener<ContextRefreshedEvent>{
	
	public static Logger logger = LoggerFactory.getLogger(RegisterServiceThread.class);
	private Thread thread;
	
	@Value("${room.register.listenPort}")
    private volatile int remoteListenerPort;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		if(this.thread == null) {
			this.thread = new Thread(this);
			this.thread.start();
			logger.debug("RegisterService Thread created.");
		}
	}

	@Override
	public void run() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException ukHostException) {
			// TODO Auto-generated catch block
			logger.error("Unknown Host Exception", ukHostException);
			return ;
		}
        Socket s1=null;
        BufferedReader br=null;
        BufferedReader is=null;
        PrintWriter os=null;
        try {
            s1=new Socket(address, this.remoteListenerPort); // You can use static final constant PORT_NUM
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
	        System.out.print("[?] Big Blind: ");
	        int blind = Integer.parseInt(br.readLine());
	        System.out.print("[?] Minimum bet: ");
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
	        srvInfo.port = this.remoteListenerPort;
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
	        	String serverIdentityHash = br.readLine();
	        	auth.server_identity_hash = serverIdentityHash;
	        	System.out.println("[>] Sending Server Identity Hash "+serverIdentityHash+"...");
	        	os.println(oM.writeValueAsString(auth));
	        	os.flush();
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
            try {
				is.close();
				os.close();
	            br.close();
	            s1.close();
	            System.out.println("Connection Closed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	public static void authorized(AuthorizationBearer auth, BufferedReader console) throws IOException {
    	System.out.println("[*] Access Granted");
    	System.out.println("[*] New Server Identity Hash: "+auth.server_identity_hash);
    	System.out.println("!!! Please save this identity for next access");
    	System.out.println("--------------------------");
    	System.out.println("Press any key to close");
    	console.read();
    }

}
