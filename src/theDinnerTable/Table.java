package theDinnerTable;

import java.util.ArrayList;

public class Table {
	
	private static final int NUM_PHILS = 5;
	private static final int NUM_TABLES = 1;
	private static final int OUNCES_OF_RICE = 64;
	
	public static void main(String[] args) {
		

		
		
	}
	
	private Philosopher[] phils;
	private ArrayList<Chopstick> chopsticks;
	private int riceBowl;
	
	Table() {
		phils = new Philosopher[NUM_PHILS];
		chopsticks = new ArrayList<Chopstick>();
		for (int i = 0; i < NUM_PHILS; i++) {
			phils[i] = new Philosopher(this, i);
			chopsticks.add(new Chopstick(i));
			riceBowl = OUNCES_OF_RICE;
		}
	}
	
	public ArrayList<Chopstick> findChopsticks(Philosopher p) {
		final int pos = p.getPos();
		final int[] range = posRange(pos);
		ArrayList<Chopstick> chops = new ArrayList<Chopstick>();
		while (chops.size() < 2) {
			for (int i : range) {
				if(!this.chopsticks.get(i).isInUse()) {
					chops.add(chopsticks.get(i));
				} else {
					synchronized (chopsticks) {
						try {
							chopsticks.wait();
						} catch (InterruptedException ignored) {}
					}
				}
			}
		}
		return chopsticks;
	}
	
	public void placeChopsticks(Chopstick[] chopstick) {
		synchronized (chopsticks) {
			chopsticks.notifyAll();
		}
		for(Chopstick c : chopsticks) {
			c.setDown();
		}
	}
	
	private int[] posRange(int pos) {
		if (pos == 0) {
			return new int[]{NUM_PHILS-1, 0, 1};
		}
		if (pos == NUM_PHILS-1) {
			return new int[] {NUM_PHILS-2,NUM_PHILS-1,0};
		}
		return new int[] {pos-1,pos,pos+1};
	}
	
	public int removeRice(int request) {
		if (request > riceBowl) {
			request = riceBowl;
			riceBowl = 0;
			return request;
		}
		else {
			riceBowl -= request;
			return request;
		}
	}
}
