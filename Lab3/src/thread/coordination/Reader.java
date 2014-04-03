package thread.coordination;

public class Reader extends Thread {

	private Mailbox mailBox;
	
	public Reader(String name, Mailbox mailBox) {
		super(name);
		this.mailBox = mailBox;
	}
	
	@Override
	public void run() {
		String msg = "";
		while (true) {
			msg = mailBox.readMessage();
			if (msg.isEmpty()) {
				System.out.println("************************************");
			}
			System.out.println(getName() + " read " + msg);
			try {
				sleep((long)(Math.random()*100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
