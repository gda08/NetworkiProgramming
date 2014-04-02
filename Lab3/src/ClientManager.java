import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class ClientManager {

	Map<String, ClientThread> clients;

	public ClientManager() {
		clients = new HashMap<String, ClientThread>();
	}

	public void addClient(String inetAdrs, Socket clientSocket) {
		if (!clients.containsKey(inetAdrs)) {
			System.out.println("Adding new client: " + inetAdrs);
			ClientThread ct = new ClientThread(clientSocket);
			clients.put(inetAdrs, ct);
			clients.get(inetAdrs).start();
			System.out.println("Client added");
		}		
	}

	public void sendBroadCastMessageToClients(String msg) {
		for (Entry<String, ClientThread> entry : clients.entrySet()) {
			entry.getValue().writeToClient(msg);
		}
	}

	public void removeClient(String inetAdrs) {
		if (clients.containsKey(inetAdrs)) {
			clients.get(inetAdrs).stopClient();
			clients.remove(inetAdrs);
		}
	}

}
