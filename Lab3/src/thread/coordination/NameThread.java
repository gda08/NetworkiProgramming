package thread.coordination;

public class NameThread extends Thread {
		
	public NameThread(String name) {
		super(name);
	}
	
	@Override
	public void run() {
		for (int i=0; i<5; i++) {
			System.out.println(i+1 + ": " + getName());
			try {
				sleep((long) (Math.random()*10000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
