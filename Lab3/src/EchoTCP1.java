import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class EchoTCP1 {

	private ServerSocket server;
	private boolean run;
	
	private ClientManager man;

	public EchoTCP1(int port) {
		try {
			server = new ServerSocket(port);
			run = true;
			man = new ClientManager();
			(new Writer()).start();
			acceptConnections();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void acceptConnections() {
		while (run) {
			try {
				System.out.println("Server waiting for clients....");
				Socket client = server.accept();
				System.out.println("Client connected: " + client.getInetAddress());

				man.addClient(client.getInetAddress().toString(), client);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Writer extends Thread {
		
		@Override
		public void run() {
			while (true) {
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    try {
					String s = bufferRead.readLine();
					man.sendBroadCastMessageToClients(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}


}
