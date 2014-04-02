import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

	private boolean run;
	private Socket client;
	private InputStream in;
	private OutputStream out;
	private BufferedReader bin;
	private PrintWriter pout;
	
	public ClientThread(Socket client) {
		this.client = client;
		try {
			in = client.getInputStream();
			out = client.getOutputStream();
			pout = new PrintWriter(out, true);
			bin = new BufferedReader(new InputStreamReader(in));
		} catch (IOException e) {
			e.printStackTrace();
		}
		run = true;
	}
	
	@Override
	public void run() {
		System.out.println(client.getInetAddress().toString() + " listening to input");
		String msg = "";
		while (run) {
			try {
				msg = bin.readLine();
				if (msg.equals("null")) {
					stopClient();
				} else {
					System.out.println("Client " +
							this.client.getInetAddress() + ":" +
							msg);
				}
			} catch (IOException e) {
				stopClient();
			}
		}
	}
	
	public void writeToClient(String msg) {
		try {
			pout.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopClient() {
		System.out.println("Closing connection");
		try {
			run = false;
			bin.close();
			pout.close();
			in.close();
			out.close();
			client.close();
		} catch (IOException e) {
			System.out.println("Could not close connection: " + e.toString());
			e.printStackTrace();
		}
		System.out.println("Cconnection closed");
	}
	
}
