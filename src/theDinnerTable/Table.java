package theDinnerTable;

public class Table {
	
	private static final int NUM_PHILS = 5;
	private static final int NUM_TABLES = 1;
	
	public static void main(String[] args) {
		

		
		
	}
	
	private Philosopher[] phils;
	private Chopstick[] chopsticks;
	
	Table() {
		phils = new Philosopher[NUM_PHILS];
		chopsticks = new Chopstick[NUM_PHILS];
		for (int i = 0; i < NUM_PHILS; i++) {
			phils[i] = new Philosopher(i);
			chopsticks[i] = new Chopstick(i);
		}
	}
	
	public Chopstick[] findChopsticks(Philosopher p) {
		final int pos = p.getPos();
		final int[] range = posRange(pos);
		while (true) {
			Chopstick[] chopsticks = new Chopstick[2];
			for (int i : range) {
				if(chopsticks.length <= 2)
					if(!this.chopsticks[i].isInUse()) {
						chopsticks[i] = this.chopsticks[i];
					}
			}
			if (chopsticks.length != 2) {
				try {
					p.wait(100);
				} catch (InterruptedException ignored) {}
			} else {
				for(Chopstick c : chopsticks) {
					c.pickUp();
				}
				return chopsticks;
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
}
