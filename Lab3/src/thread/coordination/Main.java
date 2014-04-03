package thread.coordination;

public class Main {
	
	public static void main(String[] args) {
		
		Mailbox mailBox = new Mailbox();
		
		Reader reader = new Reader("Reader", mailBox);
		reader.start();
		
		for (int i=0; i<10; i++) {			
			Writer p = new Writer("T" + (i+1), mailBox);
			p.start();			
		}
		
	}

}
