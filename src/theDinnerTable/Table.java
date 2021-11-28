package theDinnerTable;

import java.util.ArrayList;
import java.util.List;

public class Table {
	
	private static final int NUM_PHILS = 5;
	//private static final int NUM_TABLES = 1;
	private static final int OUNCES_OF_RICE = 64;
	
	public static void main(String[] args) {
		
		System.out.println("Dinner has started.");
		
		Table t = new Table();
		
		for( Philosopher p : t.phils) {
			p.startThinking();
		}
		
		for( Philosopher p : t.phils) {
			p.waitToStopThinking();
		}
		
		System.out.println("Dinner has ended.");
	}
	
	private Philosopher[] phils;
	private List<Chopstick> chopsticks;
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
	
	public List<Chopstick> findChopsticks(int pos) {
		final int[] range = posRange(pos);
		List<Chopstick> chops = new ArrayList<Chopstick>();
		synchronized (chopsticks) {
			while (chops.size() < 2) {
				for (int i : range) {
					Chopstick c = chopsticks.get(i);
					try {
						c.pickUp();
						chops.add(c);
					} catch (IllegalStateException e) {
						// Prevents Deadlock from everyone holding one stick.
						for(Chopstick chop : chops) {
							chop.setDown();
						}
						chops.clear();
						
						/* 
						 * Makes the Philosophers wait until a Chopstick is put 
						 * back on the table before trying to find two again.
						 */
						
							try {
								chopsticks.wait();
							} catch (InterruptedException ignored) {}
						}
					}
				}
		}
		return chops;
	}
	
	public void placeChopsticks(List<Chopstick> chopstick) {
		synchronized (chopsticks) {
			chopsticks.notifyAll();
		for(Chopstick c : chopsticks) {
			c.setDown();
		}
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
	
	public synchronized int removeRice(int request) {
			request = Math.min(request, riceBowl);
			riceBowl -= request;
			//System.out.println("Rice Bowl: " + riceBowl);
			return request;
	}
	
	public synchronized int getRiceBowl() {
		return riceBowl;
	}
	
}
