package thread.coordination;

public class Writer extends Thread {

	private Mailbox mailBox;
	
	public Writer(String name, Mailbox mailBox) {
		super(name);
		this.mailBox = mailBox;
	}
	
	@Override
	public void run() {
		while (true) {
			for (int i=0; i<5; i++) {
				mailBox.writeMessage(getName());
				System.out.println(getName() + " wrote his name");
			}
			try {
				sleep((long)(Math.random()*10000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 
		}
	}
	
}
