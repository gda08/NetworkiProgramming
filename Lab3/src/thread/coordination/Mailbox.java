package thread.coordination;

public class Mailbox {
	
	private String message;
	
	private Object writerLock;
	private Object readerLock;
	
	public Mailbox() {
		message = "";
		writerLock = new Object();
		readerLock = new Object();
	}
	
	public synchronized void writeMessage(String message) {
		synchronized (writerLock) {
			while (!message.isEmpty()) {
				try {
					System.out.println("A writer is waiting for the string to be empty");
					writerLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		this.message = message;
		readerLock.notify();
	}
	
	public String readMessage() {				
		synchronized (readerLock) {
			while (message.isEmpty()) {
				try {
					System.out.println("Reader is waiting for some writer to write");
					readerLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
		String temp = message;
		message = "";
		writerLock.notifyAll();
		return temp;
	}

}
